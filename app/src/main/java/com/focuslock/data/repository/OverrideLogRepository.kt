package com.focuslock.data.repository

import com.focuslock.data.db.dao.OverrideLogDao
import com.focuslock.data.db.entities.OverrideLog

class OverrideLogRepository(private val overrideLogDao: OverrideLogDao) {

    val allLogs = overrideLogDao.getAllLogs()

    suspend fun logOverride(
        packageName: String,
        appName: String,
        profileName: String,
        justification: String,
        durationMinutes: Int
    ) {
        overrideLogDao.insert(
            OverrideLog(
                packageName = packageName,
                appName = appName,
                profileName = profileName,
                justification = justification,
                durationMinutes = durationMinutes
            )
        )
    }

    suspend fun getOverrideCount(packageName: String): Int =
        overrideLogDao.countByPackage(packageName)

    suspend fun clearAll() = overrideLogDao.clearAll()
}
