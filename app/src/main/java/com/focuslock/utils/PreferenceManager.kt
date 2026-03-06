package com.focuslock.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var onboardingComplete: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_COMPLETE, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETE, value).apply()

    var defaultOverrideDurationMinutes: Int
        get() = prefs.getInt(KEY_OVERRIDE_DURATION, 5)
        set(value) = prefs.edit().putInt(KEY_OVERRIDE_DURATION, value).apply()

    /** Package name currently in override mode; empty string means no active override. */
    var activeOverridePackage: String
        get() = prefs.getString(KEY_ACTIVE_OVERRIDE_PKG, "") ?: ""
        set(value) = prefs.edit().putString(KEY_ACTIVE_OVERRIDE_PKG, value).apply()

    /** Epoch millis when the current override expires; 0 if none. */
    var activeOverrideExpiryMs: Long
        get() = prefs.getLong(KEY_ACTIVE_OVERRIDE_EXPIRY, 0L)
        set(value) = prefs.edit().putLong(KEY_ACTIVE_OVERRIDE_EXPIRY, value).apply()

    fun clearOverride() {
        prefs.edit()
            .putString(KEY_ACTIVE_OVERRIDE_PKG, "")
            .putLong(KEY_ACTIVE_OVERRIDE_EXPIRY, 0L)
            .apply()
    }

    fun isOverrideActive(packageName: String): Boolean {
        if (activeOverridePackage != packageName) return false
        return System.currentTimeMillis() < activeOverrideExpiryMs
    }

    companion object {
        private const val PREF_NAME = "focuslock_prefs"
        private const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
        private const val KEY_OVERRIDE_DURATION = "override_duration_minutes"
        private const val KEY_ACTIVE_OVERRIDE_PKG = "active_override_package"
        private const val KEY_ACTIVE_OVERRIDE_EXPIRY = "active_override_expiry"
    }
}
