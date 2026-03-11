package com.focuslock.data.repository

import com.focuslock.data.db.dao.BlockedAppDao
import com.focuslock.data.db.dao.ProfileDao
import com.focuslock.data.db.dao.ScheduleDayDao
import com.focuslock.data.db.entities.BlockedApp
import com.focuslock.data.db.entities.Profile
import com.focuslock.data.db.entities.ScheduleDay

class ProfileRepository(
    private val profileDao: ProfileDao,
    private val blockedAppDao: BlockedAppDao,
    private val scheduleDayDao: ScheduleDayDao
) {

    val allProfiles = profileDao.getAllProfiles()

    suspend fun getProfileById(id: Long): Profile? = profileDao.getProfileById(id)

    suspend fun createProfile(name: String, overrideDurationMinutes: Int = 5): Long {
        val profile = Profile(name = name, overrideDurationMinutes = overrideDurationMinutes)
        val profileId = profileDao.insert(profile)
        // Create default schedule entries for all 7 days (disabled by default)
        val defaultDays = (1..7).map { day ->
            ScheduleDay(profileId = profileId, dayOfWeek = day)
        }
        scheduleDayDao.insertAll(defaultDays)
        return profileId
    }

    suspend fun updateProfile(profile: Profile) = profileDao.update(profile)

    suspend fun deleteProfile(profile: Profile) = profileDao.delete(profile)

    suspend fun setProfileEnabled(id: Long, isEnabled: Boolean) =
        profileDao.setEnabled(id, isEnabled)

    // Blocked apps
    suspend fun getBlockedApps(profileId: Long): List<BlockedApp> =
        blockedAppDao.getBlockedAppsForProfile(profileId)

    suspend fun saveBlockedApps(profileId: Long, apps: List<BlockedApp>) {
        blockedAppDao.deleteAllForProfile(profileId)
        if (apps.isNotEmpty()) {
            blockedAppDao.insertAll(apps)
        }
    }

    // Schedule
    suspend fun getSchedule(profileId: Long): List<ScheduleDay> =
        scheduleDayDao.getScheduleForProfile(profileId)

    suspend fun saveSchedule(profileId: Long, days: List<ScheduleDay>) {
        scheduleDayDao.deleteAllForProfile(profileId)
        if (days.isNotEmpty()) {
            scheduleDayDao.insertAll(days)
        }
    }

    /**
     * Returns the set of package names that are currently blocked at this moment
     * (across all enabled profiles whose schedule is currently active).
     */
    suspend fun getCurrentlyBlockedPackages(): Set<String> {
        val enabledProfiles = profileDao.getEnabledProfiles()
        if (enabledProfiles.isEmpty()) return emptySet()

        val now = java.util.Calendar.getInstance()
        val currentDayOfWeek = now.get(java.util.Calendar.DAY_OF_WEEK)
        // Android Calendar: 1=Sun,2=Mon,...,7=Sat — convert to our 1=Mon..7=Sun
        val dayOfWeek = if (currentDayOfWeek == java.util.Calendar.SUNDAY) 7
        else currentDayOfWeek - 1
        val currentHour = now.get(java.util.Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(java.util.Calendar.MINUTE)

        val activeProfileIds = mutableListOf<Long>()
        for (profile in enabledProfiles) {
            val schedule = scheduleDayDao.getScheduleForProfile(profile.id)
            val todaySchedule = schedule.find { it.dayOfWeek == dayOfWeek }
            if (todaySchedule != null && todaySchedule.isEnabled) {
                if (isTimeInWindow(currentHour, currentMinute, todaySchedule)) {
                    activeProfileIds.add(profile.id)
                }
            }
        }

        if (activeProfileIds.isEmpty()) return emptySet()
        return blockedAppDao.getBlockedPackagesForProfiles(activeProfileIds).toSet()
    }

    private fun isTimeInWindow(hour: Int, minute: Int, schedule: ScheduleDay): Boolean {
        val currentMins = hour * 60 + minute
        val startMins = schedule.startHour * 60 + schedule.startMinute
        val endMins = schedule.endHour * 60 + schedule.endMinute

        return if (endMins > startMins) {
            // Normal window (e.g. 09:00–17:00)
            currentMins in startMins until endMins
        } else {
            // Overnight window (e.g. 22:00–07:00)
            currentMins >= startMins || currentMins < endMins
        }
    }

    /**
     * Returns profile name(s) causing the block for a given package at the current time.
     */
    suspend fun getActiveProfilesBlockingApp(packageName: String): List<String> {
        val enabledProfiles = profileDao.getEnabledProfiles()
        val now = java.util.Calendar.getInstance()
        val currentDayOfWeek = now.get(java.util.Calendar.DAY_OF_WEEK)
        val dayOfWeek = if (currentDayOfWeek == java.util.Calendar.SUNDAY) 7
        else currentDayOfWeek - 1
        val currentHour = now.get(java.util.Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(java.util.Calendar.MINUTE)

        val result = mutableListOf<String>()
        for (profile in enabledProfiles) {
            val apps = blockedAppDao.getBlockedAppsForProfile(profile.id)
            if (apps.none { it.packageName == packageName }) continue
            val schedule = scheduleDayDao.getScheduleForProfile(profile.id)
            val todaySchedule = schedule.find { it.dayOfWeek == dayOfWeek }
            if (todaySchedule != null && todaySchedule.isEnabled &&
                isTimeInWindow(currentHour, currentMinute, todaySchedule)
            ) {
                result.add(profile.name)
            }
        }
        return result
    }

    /**
     * Returns the earliest end time (hour, minute) of any currently active blocking window,
     * across all enabled profiles. Returns null if nothing is actively blocked.
     */
    suspend fun getEarliestBlockEndTime(): Pair<Int, Int>? {
        val enabledProfiles = profileDao.getEnabledProfiles()
        if (enabledProfiles.isEmpty()) return null

        val now = java.util.Calendar.getInstance()
        val currentDayOfWeek = now.get(java.util.Calendar.DAY_OF_WEEK)
        val dayOfWeek = if (currentDayOfWeek == java.util.Calendar.SUNDAY) 7
        else currentDayOfWeek - 1
        val currentHour = now.get(java.util.Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(java.util.Calendar.MINUTE)

        var earliestEndMins: Int? = null
        for (profile in enabledProfiles) {
            val schedule = scheduleDayDao.getScheduleForProfile(profile.id)
            val todaySchedule = schedule.find { it.dayOfWeek == dayOfWeek }
            if (todaySchedule != null && todaySchedule.isEnabled &&
                isTimeInWindow(currentHour, currentMinute, todaySchedule)
            ) {
                val endMins = todaySchedule.endHour * 60 + todaySchedule.endMinute
                if (earliestEndMins == null || endMins < earliestEndMins) {
                    earliestEndMins = endMins
                }
            }
        }
        return earliestEndMins?.let { Pair(it / 60, it % 60) }
    }

    /**
     * Returns the end time (hour, minute) of the blocking window for a given package.
     * Returns null if no active block.
     */
    suspend fun getBlockEndTime(packageName: String): Pair<Int, Int>? {
        val enabledProfiles = profileDao.getEnabledProfiles()
        val now = java.util.Calendar.getInstance()
        val currentDayOfWeek = now.get(java.util.Calendar.DAY_OF_WEEK)
        val dayOfWeek = if (currentDayOfWeek == java.util.Calendar.SUNDAY) 7
        else currentDayOfWeek - 1
        val currentHour = now.get(java.util.Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(java.util.Calendar.MINUTE)

        for (profile in enabledProfiles) {
            val apps = blockedAppDao.getBlockedAppsForProfile(profile.id)
            if (apps.none { it.packageName == packageName }) continue
            val schedule = scheduleDayDao.getScheduleForProfile(profile.id)
            val todaySchedule = schedule.find { it.dayOfWeek == dayOfWeek }
            if (todaySchedule != null && todaySchedule.isEnabled &&
                isTimeInWindow(currentHour, currentMinute, todaySchedule)
            ) {
                return Pair(todaySchedule.endHour, todaySchedule.endMinute)
            }
        }
        return null
    }
}
