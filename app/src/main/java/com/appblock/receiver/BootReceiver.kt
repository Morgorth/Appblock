package com.appblock.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.appblock.service.BlockerService

/**
 * Restarts the BlockerService after device reboot.
 *
 * Both BOOT_COMPLETED and LOCKED_BOOT_COMPLETED are registered so the
 * service can start before the user unlocks the device – relevant on
 * Pixel 8/9 which may reboot for security updates while unattended.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val validActions = setOf(
            Intent.ACTION_BOOT_COMPLETED,
            "android.intent.action.LOCKED_BOOT_COMPLETED"
        )
        if (intent.action !in validActions) return

        val serviceIntent = Intent(context, BlockerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}
