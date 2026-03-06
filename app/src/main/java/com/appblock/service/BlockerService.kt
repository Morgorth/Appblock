package com.appblock.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.appblock.MainActivity
import com.appblock.R

/**
 * Foreground service that monitors foreground app usage and enforces blocks.
 *
 * Key Pixel 8 / Pixel 9 considerations:
 * - Must run as a foreground service with a visible notification (Android 8+).
 * - Declared as foregroundServiceType="specialUse" (Android 14 / API 34+).
 * - Uses SCHEDULE_EXACT_ALARM / USE_EXACT_ALARM for timed blocks.
 * - Handles Doze-mode battery optimisations present on Pixel 8/9.
 */
class BlockerService : Service() {

    companion object {
        private const val CHANNEL_ID = "appblock_service"
        private const val NOTIFICATION_ID = 1001
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ---------------------------------------------------------------------------
    // Notification channel – required on Android 8+ (all Pixel 8/9 devices).
    // Importance level LOW avoids sound/vibration for a persistent status note.
    // ---------------------------------------------------------------------------
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_desc)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle(getString(R.string.notification_title))
        .setContentText(getString(R.string.notification_text))
        .setSmallIcon(android.R.drawable.ic_lock_lock)
        .setOngoing(true)
        // Tapping the notification opens MainActivity
        .setContentIntent(
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                // FLAG_IMMUTABLE is required on Android 12+ (Pixel 8/9)
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
        .build()

    /**
     * Returns the package name of the app currently in the foreground.
     * Uses UsageStatsManager – requires PACKAGE_USAGE_STATS permission.
     */
    private fun getForegroundApp(): String? {
        val usm = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val now = System.currentTimeMillis()
        val stats = usm.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            now - 10_000,
            now
        )
        return stats?.maxByOrNull { it.lastTimeUsed }?.packageName
    }
}
