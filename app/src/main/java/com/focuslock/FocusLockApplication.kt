package com.focuslock

import android.app.Application
import com.focuslock.data.db.AppDatabase
import com.focuslock.data.repository.MuteRuleRepository
import com.focuslock.data.repository.OverrideLogRepository
import com.focuslock.data.repository.ProfileRepository
import com.focuslock.services.BlockerWatchdogWorker

class FocusLockApplication : Application() {

    private val database by lazy { AppDatabase.getInstance(this) }

    val profileRepository by lazy {
        ProfileRepository(
            database.profileDao(),
            database.blockedAppDao(),
            database.scheduleDayDao()
        )
    }

    val overrideLogRepository by lazy {
        OverrideLogRepository(database.overrideLogDao())
    }

    val muteRuleRepository by lazy {
        MuteRuleRepository(
            database.muteRuleDao(),
            database.mutedAppDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        BlockerWatchdogWorker.schedule(this)
    }
}
