package com.focuslock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.focuslock.FocusLockApplication
import com.focuslock.data.db.entities.BlockedApp
import com.focuslock.data.db.entities.Profile
import com.focuslock.data.db.entities.ScheduleDay
import kotlinx.coroutines.launch

class ProfileEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as FocusLockApplication).profileRepository

    private val _profile = MutableLiveData<Profile?>()
    val profile: LiveData<Profile?> = _profile

    private val _blockedApps = MutableLiveData<List<BlockedApp>>(emptyList())
    val blockedApps: LiveData<List<BlockedApp>> = _blockedApps

    private val _scheduleDays = MutableLiveData<List<ScheduleDay>>(emptyList())
    val scheduleDays: LiveData<List<ScheduleDay>> = _scheduleDays

    private val _saveComplete = MutableLiveData(false)
    val saveComplete: LiveData<Boolean> = _saveComplete

    var currentProfileId: Long = -1L
        private set

    fun loadProfile(profileId: Long) {
        if (profileId == -1L) {
            // New profile
            _profile.value = null
            _scheduleDays.value = (1..7).map { day ->
                ScheduleDay(profileId = -1, dayOfWeek = day)
            }
            return
        }
        currentProfileId = profileId
        viewModelScope.launch {
            val p = repository.getProfileById(profileId)
            _profile.postValue(p)
            val apps = repository.getBlockedApps(profileId)
            _blockedApps.postValue(apps)
            val days = repository.getSchedule(profileId)
            _scheduleDays.postValue(days)
        }
    }

    fun updateScheduleDay(updated: ScheduleDay) {
        val current = _scheduleDays.value?.toMutableList() ?: mutableListOf()
        val index = current.indexOfFirst { it.dayOfWeek == updated.dayOfWeek }
        if (index >= 0) current[index] = updated else current.add(updated)
        _scheduleDays.value = current
    }

    fun setBlockedApps(apps: List<BlockedApp>) {
        _blockedApps.value = apps
    }

    fun saveProfile(name: String, overrideDuration: Int) {
        viewModelScope.launch {
            val profileId: Long
            if (currentProfileId == -1L) {
                // Create new
                profileId = repository.createProfile(name, overrideDuration)
                currentProfileId = profileId
            } else {
                profileId = currentProfileId
                val existing = repository.getProfileById(profileId)
                if (existing != null) {
                    repository.updateProfile(
                        existing.copy(
                            name = name,
                            overrideDurationMinutes = overrideDuration
                        )
                    )
                }
            }

            // Save blocked apps (re-stamp profileId)
            val apps = _blockedApps.value?.map { it.copy(profileId = profileId) } ?: emptyList()
            repository.saveBlockedApps(profileId, apps)

            // Save schedule days (re-stamp profileId)
            val days = _scheduleDays.value?.map { it.copy(profileId = profileId) } ?: emptyList()
            repository.saveSchedule(profileId, days)

            _saveComplete.postValue(true)
        }
    }
}
