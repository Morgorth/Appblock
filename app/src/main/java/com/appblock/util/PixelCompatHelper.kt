package com.appblock.util

import android.content.Context
import android.os.Build
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Centralises Pixel 8 / Pixel 9 display compatibility helpers.
 *
 * Pixel 8  → Android 14 (API 34)
 * Pixel 9  → Android 15 (API 35)
 *
 * Both devices share:
 *  - A centred punch-hole (hole-punch) front camera
 *  - Rounded display corners (radius ~108 px on Pixel 8, ~114 px on Pixel 9)
 *  - Gesture navigation bar by default
 *  - Material You dynamic colour support
 */
object PixelCompatHelper {

    /**
     * Call once in Activity.onCreate() (before setContentView).
     *
     * Enables edge-to-edge rendering so the app draws behind the status bar
     * and navigation bar.  This is opt-in on Android 14 but *enforced* on
     * Android 15, so we always enable it to behave consistently on both
     * Pixel 8 and Pixel 9.
     */
    fun enableEdgeToEdge(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    /**
     * Sets the appearance of the status-bar icons (light/dark) to match the
     * app background so they are readable on both the light Pixel launcher
     * theme and dark mode.
     *
     * @param isLightBackground true if the area behind the status bar is light
     */
    fun configureStatusBarAppearance(window: Window, isLightBackground: Boolean) {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = isLightBackground
            isAppearanceLightNavigationBars = isLightBackground
        }
    }

    /**
     * Returns true when running on a Google Pixel 8 or Pixel 9 device.
     * Used to gate any device-specific workarounds.
     */
    fun isPixel8or9(): Boolean {
        val model = Build.MODEL.lowercase()
        // Pixel 8: "pixel 8" / "pixel 8 pro"
        // Pixel 9: "pixel 9" / "pixel 9 pro" / "pixel 9 pro xl" / "pixel 9 pro fold"
        return Build.MANUFACTURER.lowercase() == "google" &&
                (model.startsWith("pixel 8") || model.startsWith("pixel 9"))
    }

    /**
     * Android 15 (Pixel 9) made edge-to-edge non-optional.
     * Returns true if the current OS enforces edge-to-edge.
     */
    fun isEdgeToEdgeEnforced(): Boolean = Build.VERSION.SDK_INT >= 35
}
