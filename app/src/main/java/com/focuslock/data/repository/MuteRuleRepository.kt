package com.focuslock.data.repository

import com.focuslock.data.db.dao.MutedAppDao
import com.focuslock.data.db.dao.MuteRuleDao
import com.focuslock.data.db.entities.MutedApp
import com.focuslock.data.db.entities.MuteRule
import com.focuslock.data.db.entities.MuteScheduleType
import java.util.Calendar

class MuteRuleRepository(
    private val muteRuleDao: MuteRuleDao,
    private val mutedAppDao: MutedAppDao
) {

    val allRules = muteRuleDao.getAllRules()

    suspend fun createRule(rule: MuteRule, apps: List<MutedApp>): Long {
        val ruleId = muteRuleDao.insert(rule)
        if (apps.isNotEmpty()) {
            mutedAppDao.insertAll(apps.map { it.copy(muteRuleId = ruleId) })
        }
        return ruleId
    }

    suspend fun updateRule(rule: MuteRule, apps: List<MutedApp>) {
        muteRuleDao.update(rule)
        mutedAppDao.deleteAllForRule(rule.id)
        if (apps.isNotEmpty()) {
            mutedAppDao.insertAll(apps.map { it.copy(muteRuleId = rule.id) })
        }
    }

    suspend fun deleteRule(rule: MuteRule) = muteRuleDao.delete(rule)

    suspend fun setRuleEnabled(id: Long, isEnabled: Boolean) =
        muteRuleDao.setEnabled(id, isEnabled)

    suspend fun getAppsForRule(ruleId: Long): List<MutedApp> =
        mutedAppDao.getAppsForRule(ruleId)

    suspend fun getRuleById(id: Long): MuteRule? = muteRuleDao.getRuleById(id)

    /**
     * Returns the set of package names whose notifications should be muted right now,
     * based on all enabled mute rules and the current time.
     * Called from NotificationMuterService every 30s to refresh the in-memory cache.
     */
    suspend fun getActivelyMutedPackages(): Set<String> {
        val enabledRules = muteRuleDao.getEnabledRulesSync()
        if (enabledRules.isEmpty()) return emptySet()

        val now = Calendar.getInstance()
        val nowMs = now.timeInMillis
        val rawDay = now.get(Calendar.DAY_OF_WEEK)
        val dayOfWeek = if (rawDay == Calendar.SUNDAY) 7 else rawDay - 1 // 1=Mon..7=Sun
        val isWeekend = dayOfWeek >= 6
        val currentMins = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)

        val activeRuleIds = mutableListOf<Long>()
        for (rule in enabledRules) {
            val active = when (rule.scheduleType) {
                MuteScheduleType.DATE_RANGE -> nowMs in rule.startDateMs..rule.endDateMs
                MuteScheduleType.WEEKDAYS   -> !isWeekend && isInHourRange(rule, currentMins)
                MuteScheduleType.WEEKENDS   -> isWeekend && isInHourRange(rule, currentMins)
                MuteScheduleType.EVERY_DAY  -> isInHourRange(rule, currentMins)
            }
            if (active) activeRuleIds.add(rule.id)
        }

        if (activeRuleIds.isEmpty()) return emptySet()
        return mutedAppDao.getPackagesForRules(activeRuleIds).toSet()
    }

    private fun isInHourRange(rule: MuteRule, currentMins: Int): Boolean {
        if (!rule.useHourRange) return true
        val startMins = rule.startHour * 60 + rule.startMinute
        val endMins   = rule.endHour * 60 + rule.endMinute
        return if (endMins > startMins) currentMins in startMins until endMins
        else currentMins >= startMins || currentMins < endMins
    }
}
