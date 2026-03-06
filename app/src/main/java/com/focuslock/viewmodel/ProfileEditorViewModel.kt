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

    /** Days that have been "singled out" and use their own independent time window. */
    private val _pinnedDays = MutableLiveData<Set<Int>>(emptySet())
    val pinnedDays: LiveData<Set<Int>> = _pinnedDays

    private val _saveComplete = MutableLiveData(false)
    val saveComplete: LiveData<Boolean> = _saveComplete

    var currentProfileId: Long = -1L
        private set

    // Shared time applied to all non-pinned days.
    private var sharedStartHour: Int = 9
    private var sharedStartMinute: Int = 0
    private var sharedEndHour: Int = 17
    private var sharedEndMinute: Int = 0

    fun loadProfile(profileId: Long) {
        if (profileId == -1L) {
            _profile.value = null
            _scheduleDays.value = (1..7).map { day ->
                ScheduleDay(
                    profileId = -1, dayOfWeek = day,
                    startHour = sharedStartHour, startMinute = sharedStartMinute,
                    endHour = sharedEndHour, endMinute = sharedEndMinute
                )
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
            // Seed shared time from the first enabled day (fallback: defaults).
            val firstEnabled = days.firstOrNull { it.isEnabled }
            if (firstEnabled != null) {
                sharedStartHour = firstEnabled.startHour
                sharedStartMinute = firstEnabled.startMinute
                sharedEndHour = firstEnabled.endHour
                sharedEndMinute = firstEnabled.endMinute
            }
            _scheduleDays.postValue(days)
        }
    }

    /**
     * Toggle a day between "synced" (shares the global time window) and
     * "custom" (has its own independent time window).
     *
     * Unpinning a day resets it to the current shared time.
     */
    fun toggleDayPin(dayOfWeek: Int) {
        val current = _pinnedDays.value?.toMutableSet() ?: mutableSetOf()
        if (dayOfWeek in current) {
            // Un-pin: sync this day back to the shared window.
            current.remove(dayOfWeek)
            val days = _scheduleDays.value?.toMutableList() ?: mutableListOf()
            val idx = days.indexOfFirst { it.dayOfWeek == dayOfWeek }
            if (idx >= 0) {
                days[idx] = days[idx].copy(
                    startHour = sharedStartHour, startMinute = sharedStartMinute,
                    endHour = sharedEndHour, endMinute = sharedEndMinute
                )
                _scheduleDays.value = days
            }
        } else {
            // Pin: this day now owns its own time — no time change yet.
            current.add(dayOfWeek)
        }
        _pinnedDays.value = current
    }

    /**
     * Change the shared time window and propagate it to every non-pinned day.
     * Call this when the user edits a time picker on a synced day.
     */
    fun updateSharedTime(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) {
        sharedStartHour = startHour
        sharedStartMinute = startMinute
        sharedEndHour = endHour
        sharedEndMinute = endMinute
        val pinned = _pinnedDays.value ?: emptySet()
        _scheduleDays.value = _scheduleDays.value?.map { day ->
            if (day.dayOfWeek !in pinned) {
                day.copy(
                    startHour = startHour, startMinute = startMinute,
                    endHour = endHour, endMinute = endMinute
                )
            } else day
        }
    }

    /**
     * Update a single day without touching other days.
     * Used for: enable/disable toggle, and time changes on pinned days.
     */
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

            val apps = _blockedApps.value?.map { it.copy(profileId = profileId) } ?: emptyList()
            repository.saveBlockedApps(profileId, apps)

            val days = _scheduleDays.value?.map { it.copy(profileId = profileId) } ?: emptyList()
            repository.saveSchedule(profileId, days)

            _saveComplete.postValue(true)
        }
    }
}
