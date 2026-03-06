package com.focuslock

import android.app.Application
import com.focuslock.data.db.AppDatabase
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

    override fun onCreate() {
        super.onCreate()
        // Schedule the watchdog worker on every app start
        BlockerWatchdogWorker.schedule(this)
    }
}
