package com.focuslock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.focuslock.FocusLockApplication
import com.focuslock.data.db.entities.Profile
import com.focuslock.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as FocusLockApplication).profileRepository
    private val overrideLogRepository = (application as FocusLockApplication).overrideLogRepository
    private val muteRuleRepository = (application as FocusLockApplication).muteRuleRepository

    val profiles = repository.allProfiles
    val muteRules = muteRuleRepository.allRules

    data class ActivitySummary(
        val blockedPackages: List<String>,
        val freeAtTime: String?,   // "HH:mm" when blocking; null = nothing blocked right now
        val totalOverrides: Int
    )

    val activitySummary: LiveData<ActivitySummary> = liveData(Dispatchers.IO) {
        while (true) {
            val blocked = repository.getCurrentlyBlockedPackages().toList()
            val freeAt = if (blocked.isNotEmpty()) {
                repository.getEarliestBlockEndTime()?.let { (h, m) -> TimeUtils.formatTime(h, m) }
            } else null
            val overrides = overrideLogRepository.getTotalCount()
            emit(ActivitySummary(blocked, freeAt, overrides))
            delay(30_000L)
        }
    }

    fun toggleProfile(profile: Profile, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.setProfileEnabled(profile.id, isEnabled)
        }
    }

    fun deleteProfile(profile: Profile) {
        viewModelScope.launch {
            repository.deleteProfile(profile)
        }
    }

    fun toggleMuteRule(ruleId: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            muteRuleRepository.setRuleEnabled(ruleId, isEnabled)
        }
    }

    fun deleteMuteRule(rule: com.focuslock.data.db.entities.MuteRule) {
        viewModelScope.launch {
            muteRuleRepository.deleteRule(rule)
        }
    }
}
