package com.focuslock.services

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.focuslock.FocusLockApplication
import com.focuslock.ui.overlay.BlockOverlayActivity
import com.focuslock.utils.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Accessibility service used as a fallback / complement to the UsageStats polling.
 * On OEMs that restrict UsageStatsManager (Xiaomi MIUI, Huawei EMUI), accessibility events
 * provide reliable window-change callbacks without polling.
 *
 * When the user switches to a blocked app, this service fires TYPE_WINDOW_STATE_CHANGED
 * and can immediately launch the overlay — no polling delay.
 */
class FocusLockAccessibilityService : AccessibilityService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var prefManager: PreferenceManager
    private lateinit var profileRepository: com.focuslock.data.repository.ProfileRepository

    override fun onServiceConnected() {
        super.onServiceConnected()
        prefManager = PreferenceManager(this)
        profileRepository = (application as FocusLockApplication).profileRepository
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val packageName = event.packageName?.toString() ?: return

        // Skip our own package
        if (packageName == this.packageName) return

        scope.launch {
            // Check override first
            if (prefManager.isOverrideActive(packageName)) return@launch

            val blockedPackages = profileRepository.getCurrentlyBlockedPackages()
            if (packageName in blockedPackages) {
                val intent = Intent(
                    this@FocusLockAccessibilityService,
                    BlockOverlayActivity::class.java
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra(BlockOverlayActivity.EXTRA_PACKAGE_NAME, packageName)
                }
                startActivity(intent)
            }
        }
    }

    override fun onInterrupt() {
        // No-op: required by AccessibilityService
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
