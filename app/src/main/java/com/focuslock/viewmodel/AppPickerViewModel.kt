package com.focuslock.viewmodel

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.focuslock.data.db.entities.BlockedApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class AppItem(
    val packageName: String,
    val appName: String,
    var isSelected: Boolean = false
)

class AppPickerViewModel(application: Application) : AndroidViewModel(application) {

    private val pm: PackageManager = application.packageManager

    private val _apps = MutableLiveData<List<AppItem>>(emptyList())
    val apps: LiveData<List<AppItem>> = _apps

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private var allApps: List<AppItem> = emptyList()
    private var showSystemApps = false

    fun loadApps(alreadyBlockedPackages: Set<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { app ->
                    if (showSystemApps) true
                    else (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0
                }
                .map { app ->
                    AppItem(
                        packageName = app.packageName,
                        appName = pm.getApplicationLabel(app).toString(),
                        isSelected = app.packageName in alreadyBlockedPackages
                    )
                }
                .sortedBy { it.appName.lowercase() }

            allApps = installedApps
            _apps.postValue(installedApps)
            _isLoading.postValue(false)
        }
    }

    fun filter(query: String) {
        val filtered = if (query.isBlank()) allApps
        else allApps.filter { it.appName.contains(query, ignoreCase = true) }
        _apps.value = filtered
    }

    fun toggleSelection(packageName: String) {
        allApps = allApps.map { app ->
            if (app.packageName == packageName) app.copy(isSelected = !app.isSelected) else app
        }
        // Re-apply current filter
        _apps.value = _apps.value?.map { app ->
            if (app.packageName == packageName) app.copy(isSelected = !app.isSelected) else app
        }
    }

    fun toggleSystemApps(show: Boolean, alreadyBlocked: Set<String>) {
        showSystemApps = show
        loadApps(alreadyBlocked)
    }

    fun getSelectedApps(profileId: Long): List<BlockedApp> {
        return allApps
            .filter { it.isSelected }
            .map { BlockedApp(profileId = profileId, packageName = it.packageName, appName = it.appName) }
    }
}
