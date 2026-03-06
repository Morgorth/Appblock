package com.focuslock.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

/**
 * Utilities for detecting OEMs that aggressively kill background services
 * and providing deep-links to their battery whitelisting settings.
 */
object OemUtils {

    enum class Oem { XIAOMI, HUAWEI, SAMSUNG, OPPO, VIVO, ONEPLUS, OTHER }

    val currentOem: Oem by lazy {
        val manufacturer = Build.MANUFACTURER.lowercase()
        when {
            manufacturer.contains("xiaomi") || manufacturer.contains("redmi") -> Oem.XIAOMI
            manufacturer.contains("huawei") || manufacturer.contains("honor") -> Oem.HUAWEI
            manufacturer.contains("samsung") -> Oem.SAMSUNG
            manufacturer.contains("oppo") -> Oem.OPPO
            manufacturer.contains("vivo") -> Oem.VIVO
            manufacturer.contains("oneplus") -> Oem.ONEPLUS
            else -> Oem.OTHER
        }
    }

    /** Returns true if the OEM is known to aggressively kill services. */
    val requiresBatteryWhitelist: Boolean
        get() = currentOem != Oem.OTHER

    /**
     * Returns an intent for the OEM-specific battery settings screen, or null if unavailable.
     * Falls back to generic battery optimization request.
     */
    fun getBatteryWhitelistIntent(context: Context): Intent? {
        val intents = getOemIntents()
        for (intent in intents) {
            if (isIntentAvailable(context, intent)) return intent
        }
        // Fall back to standard
        return PermissionUtils.getBatteryOptimizationIntent(context).takeIf {
            isIntentAvailable(context, it)
        }
    }

    private fun getOemIntents(): List<Intent> = when (currentOem) {
        Oem.XIAOMI -> listOf(
            Intent().setComponent(
                ComponentName(
                    "com.miui.powerkeeper",
                    "com.miui.powerkeeper.ui.HiddenAppsConfigActivity"
                )
            ),
            Intent("miui.intent.action.APP_PERM_EDITOR").apply {
                setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
                )
            }
        )
        Oem.HUAWEI -> listOf(
            Intent().setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                )
            ),
            Intent().setComponent(
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.process.ProtectActivity"
                )
            )
        )
        Oem.SAMSUNG -> listOf(
            Intent().setComponent(
                ComponentName(
                    "com.samsung.android.lool",
                    "com.samsung.android.sm.ui.battery.BatteryActivity"
                )
            )
        )
        Oem.OPPO -> listOf(
            Intent().setComponent(
                ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                )
            )
        )
        Oem.VIVO -> listOf(
            Intent().setComponent(
                ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                )
            )
        )
        Oem.ONEPLUS -> listOf(
            Intent().setComponent(
                ComponentName(
                    "com.oneplus.security",
                    "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity"
                )
            )
        )
        Oem.OTHER -> emptyList()
    }

    private fun isIntentAvailable(context: Context, intent: Intent): Boolean {
        return try {
            context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
        } catch (e: Exception) {
            false
        }
    }
}
