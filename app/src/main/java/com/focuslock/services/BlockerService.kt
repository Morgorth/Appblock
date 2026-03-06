package com.focuslock.services

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.lifecycle.LifecycleService
import com.focuslock.FocusLockApplication
import com.focuslock.ui.overlay.BlockOverlayActivity
import com.focuslock.utils.NotificationUtils
import com.focuslock.utils.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Persistent foreground service that polls the foreground app every 1500ms using
 * UsageStatsManager. When a blocked app is detected during an active schedule window,
 * it launches the BlockOverlayActivity above it.
 *
 * Also manages the override countdown timer: tracks when an override expires and
 * sends periodic notification updates.
 */
class BlockerService : LifecycleService() {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private lateinit var prefManager: PreferenceManager
    private lateinit var usageStatsManager: UsageStatsManager
    private lateinit var profileRepository: com.focuslock.data.repository.ProfileRepository

    private var pollingJob: Job? = null
    private var overrideCountdownJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        prefManager = PreferenceManager(this)
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        profileRepository = (application as FocusLockApplication).profileRepository

        NotificationUtils.createChannels(this)
        startForeground(
            NotificationUtils.NOTIF_ID_BLOCKER,
            NotificationUtils.buildBlockerNotification(this)
        )

        startPolling()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            ACTION_START_OVERRIDE -> {
                val pkg = intent.getStringExtra(EXTRA_PACKAGE_NAME) ?: return START_STICKY
                val appName = intent.getStringExtra(EXTRA_APP_NAME) ?: pkg
                val durationMs = intent.getLongExtra(EXTRA_DURATION_MS, 5 * 60 * 1000L)
                startOverrideCountdown(pkg, appName, durationMs)
            }
            ACTION_STOP_OVERRIDE -> {
                stopOverrideCountdown()
            }
        }

        return START_STICKY
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = serviceScope.launch {
            while (true) {
                checkForegroundApp()
                delay(POLL_INTERVAL_MS)
            }
        }
    }

    private suspend fun checkForegroundApp() {
        val foregroundPackage = getForegroundApp() ?: return

        // Skip our own package
        if (foregroundPackage == packageName) return

        // Check if active override is in effect
        if (prefManager.isOverrideActive(foregroundPackage)) return

        val blockedPackages = profileRepository.getCurrentlyBlockedPackages()
        if (foregroundPackage in blockedPackages) {
            showBlockOverlay(foregroundPackage)
        }
    }

    private fun getForegroundApp(): String? {
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 3000L // look back 3 seconds
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, beginTime, endTime
        )
        return stats?.maxByOrNull { it.lastTimeUsed }?.packageName
    }

    private fun showBlockOverlay(packageName: String) {
        val intent = Intent(this, BlockOverlayActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(BlockOverlayActivity.EXTRA_PACKAGE_NAME, packageName)
        }
        startActivity(intent)
    }

    private fun startOverrideCountdown(packageName: String, appName: String, durationMs: Long) {
        overrideCountdownJob?.cancel()
        val expiryMs = System.currentTimeMillis() + durationMs
        prefManager.activeOverridePackage = packageName
        prefManager.activeOverrideExpiryMs = expiryMs

        overrideCountdownJob = serviceScope.launch {
            var remainingMs = durationMs
            while (remainingMs > 0) {
                val remainingSecs = (remainingMs / 1000).toInt()
                NotificationUtils.showOverrideCountdownNotification(
                    this@BlockerService, appName, remainingSecs
                )
                delay(1000L)
                remainingMs = expiryMs - System.currentTimeMillis()
            }
            // Override expired — reinstate block
            prefManager.clearOverride()
            NotificationUtils.cancelOverrideNotification(this@BlockerService)
        }
    }

    private fun stopOverrideCountdown() {
        overrideCountdownJob?.cancel()
        prefManager.clearOverride()
        NotificationUtils.cancelOverrideNotification(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    companion object {
        private const val POLL_INTERVAL_MS = 1500L

        const val ACTION_START_OVERRIDE = "com.focuslock.ACTION_START_OVERRIDE"
        const val ACTION_STOP_OVERRIDE = "com.focuslock.ACTION_STOP_OVERRIDE"
        const val EXTRA_PACKAGE_NAME = "extra_package_name"
        const val EXTRA_APP_NAME = "extra_app_name"
        const val EXTRA_DURATION_MS = "extra_duration_ms"

        fun start(context: Context) {
            val intent = Intent(context, BlockerService::class.java)
            context.startForegroundService(intent)
        }

        fun startOverride(
            context: Context,
            packageName: String,
            appName: String,
            durationMinutes: Int
        ) {
            val intent = Intent(context, BlockerService::class.java).apply {
                action = ACTION_START_OVERRIDE
                putExtra(EXTRA_PACKAGE_NAME, packageName)
                putExtra(EXTRA_APP_NAME, appName)
                putExtra(EXTRA_DURATION_MS, durationMinutes * 60 * 1000L)
            }
            context.startForegroundService(intent)
        }
    }
}
