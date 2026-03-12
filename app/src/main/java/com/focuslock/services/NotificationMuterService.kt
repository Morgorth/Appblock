package com.focuslock.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.focuslock.FocusLockApplication
import kotlinx.coroutines.*

/**
 * Intercepts every incoming notification. If the source package is in the
 * currently-active mute rules, cancels it immediately — before the heads-up
 * banner has a chance to render on screen.
 *
 * The muted package set is refreshed every 30s in a background coroutine so
 * the hot path (onNotificationPosted) stays synchronous and near-instant.
 */
class NotificationMuterService : NotificationListenerService() {

    @Volatile
    private var mutedPackages: Set<String> = emptySet()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val repository by lazy {
        (application as FocusLockApplication).muteRuleRepository
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        startCacheRefreshLoop()
    }

    private fun startCacheRefreshLoop() {
        scope.launch {
            while (isActive) {
                mutedPackages = repository.getActivelyMutedPackages()
                delay(30_000L)
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName == packageName) return  // never suppress our own notifications
        if (sbn.packageName in mutedPackages) {
            cancelNotification(sbn.key)
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
