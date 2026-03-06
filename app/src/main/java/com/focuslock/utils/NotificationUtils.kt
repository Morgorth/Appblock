package com.focuslock.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.focuslock.R
import com.focuslock.ui.main.MainActivity

object NotificationUtils {

    const val CHANNEL_BLOCKER = "focuslock_blocker"
    const val CHANNEL_OVERRIDE = "focuslock_override"

    const val NOTIF_ID_BLOCKER = 1001
    const val NOTIF_ID_OVERRIDE = 1002

    fun createChannels(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val blockerChannel = NotificationChannel(
            CHANNEL_BLOCKER,
            "FocusLock Active",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shown while FocusLock is monitoring your apps"
            setShowBadge(false)
        }

        val overrideChannel = NotificationChannel(
            CHANNEL_OVERRIDE,
            "Override Countdown",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Countdown timer shown during an emergency override"
        }

        nm.createNotificationChannels(listOf(blockerChannel, overrideChannel))
    }

    fun buildBlockerNotification(context: Context): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, CHANNEL_BLOCKER)
            .setContentTitle(context.getString(R.string.notif_blocker_title))
            .setContentText(context.getString(R.string.notif_blocker_text))
            .setSmallIcon(R.drawable.ic_shield)
            .setContentIntent(pi)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    fun showOverrideCountdownNotification(
        context: Context,
        appName: String,
        remainingSeconds: Int
    ) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val minutes = remainingSeconds / 60
        val secs = remainingSeconds % 60
        val timeStr = String.format("%d:%02d", minutes, secs)

        val notif = NotificationCompat.Builder(context, CHANNEL_OVERRIDE)
            .setContentTitle(context.getString(R.string.notif_override_title, appName))
            .setContentText(context.getString(R.string.notif_override_text, timeStr))
            .setSmallIcon(R.drawable.ic_shield)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        nm.notify(NOTIF_ID_OVERRIDE, notif)
    }

    fun cancelOverrideNotification(context: Context) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.cancel(NOTIF_ID_OVERRIDE)
    }
}
