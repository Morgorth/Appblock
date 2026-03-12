package com.focuslock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.focuslock.FocusLockApplication
import com.focuslock.data.db.entities.MutedApp
import com.focuslock.data.db.entities.MuteRule
import com.focuslock.data.db.entities.MuteScheduleType
import kotlinx.coroutines.launch

class MuteRuleEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as FocusLockApplication).muteRuleRepository

    // Editing state
    var ruleId: Long = -1L
    var scheduleType: MuteScheduleType = MuteScheduleType.DATE_RANGE
    var startDateMs: Long = System.currentTimeMillis()
    var endDateMs: Long = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
    var useHourRange: Boolean = false
    var startHour: Int = 9
    var startMinute: Int = 0
    var endHour: Int = 18
    var endMinute: Int = 0

    val mutedApps = MutableLiveData<List<MutedApp>>(emptyList())
    val saveComplete = MutableLiveData(false)

    fun loadRule(id: Long) {
        if (id == -1L) return
        ruleId = id
        viewModelScope.launch {
            val rule = repository.getRuleById(id) ?: return@launch
            scheduleType = rule.scheduleType
            startDateMs = rule.startDateMs
            endDateMs = rule.endDateMs
            useHourRange = rule.useHourRange
            startHour = rule.startHour; startMinute = rule.startMinute
            endHour = rule.endHour; endMinute = rule.endMinute
            mutedApps.postValue(repository.getAppsForRule(id))
        }
    }

    fun setMutedApps(apps: List<MutedApp>) {
        mutedApps.value = apps
    }

    fun saveRule(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val rule = MuteRule(
                id = if (ruleId == -1L) 0 else ruleId,
                name = name,
                scheduleType = scheduleType,
                startDateMs = startDateMs,
                endDateMs = endDateMs,
                useHourRange = useHourRange,
                startHour = startHour, startMinute = startMinute,
                endHour = endHour, endMinute = endMinute
            )
            val apps = mutedApps.value ?: emptyList()
            if (ruleId == -1L) repository.createRule(rule, apps)
            else repository.updateRule(rule, apps)
            saveComplete.postValue(true)
        }
    }
}
