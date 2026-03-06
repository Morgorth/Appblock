package com.focuslock.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.focuslock.utils.PermissionUtils

/**
 * Receives BOOT_COMPLETED and MY_PACKAGE_REPLACED broadcasts to restart the
 * BlockerService after device reboot or app update.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                if (PermissionUtils.allCorePermissionsGranted(context)) {
                    BlockerService.start(context)
                }
                // Also schedule the watchdog worker
                BlockerWatchdogWorker.schedule(context)
            }
        }
    }
}
