package com.focuslock

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.focuslock.utils.PreferenceManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferenceManagerTest {

    private lateinit var prefs: PreferenceManager

    @Before
    fun setUp() {
        prefs = PreferenceManager(ApplicationProvider.getApplicationContext())
        // Start each test with a clean override state
        prefs.clearOverride()
        prefs.onboardingComplete = false
        prefs.defaultOverrideDurationMinutes = 5
    }

    // ── onboardingComplete ────────────────────────────────────────────────────

    @Test
    fun onboardingComplete_defaultIsFalse() {
        assertFalse(prefs.onboardingComplete)
    }

    @Test
    fun onboardingComplete_setTrue_persistsTrue() {
        prefs.onboardingComplete = true
        assertTrue(prefs.onboardingComplete)
    }

    @Test
    fun onboardingComplete_setFalseAfterTrue_returnsFalse() {
        prefs.onboardingComplete = true
        prefs.onboardingComplete = false
        assertFalse(prefs.onboardingComplete)
    }

    // ── defaultOverrideDurationMinutes ────────────────────────────────────────

    @Test
    fun defaultOverrideDuration_defaultIsFive() {
        assertEquals(5, prefs.defaultOverrideDurationMinutes)
    }

    @Test
    fun defaultOverrideDuration_setCustomValue() {
        prefs.defaultOverrideDurationMinutes = 15
        assertEquals(15, prefs.defaultOverrideDurationMinutes)
    }

    @Test
    fun defaultOverrideDuration_setMinimumOne() {
        prefs.defaultOverrideDurationMinutes = 1
        assertEquals(1, prefs.defaultOverrideDurationMinutes)
    }

    @Test
    fun defaultOverrideDuration_setMaximumThirty() {
        prefs.defaultOverrideDurationMinutes = 30
        assertEquals(30, prefs.defaultOverrideDurationMinutes)
    }

    // ── activeOverridePackage ─────────────────────────────────────────────────

    @Test
    fun activeOverridePackage_defaultIsEmpty() {
        assertEquals("", prefs.activeOverridePackage)
    }

    @Test
    fun activeOverridePackage_setAndRead() {
        prefs.activeOverridePackage = "com.example.app"
        assertEquals("com.example.app", prefs.activeOverridePackage)
    }

    // ── activeOverrideExpiryMs ────────────────────────────────────────────────

    @Test
    fun activeOverrideExpiry_defaultIsZero() {
        assertEquals(0L, prefs.activeOverrideExpiryMs)
    }

    @Test
    fun activeOverrideExpiry_setFutureTimestamp() {
        val future = System.currentTimeMillis() + 5 * 60 * 1000L
        prefs.activeOverrideExpiryMs = future
        assertEquals(future, prefs.activeOverrideExpiryMs)
    }

    // ── clearOverride ─────────────────────────────────────────────────────────

    @Test
    fun clearOverride_resetsPackageToEmpty() {
        prefs.activeOverridePackage = "com.blocked"
        prefs.activeOverrideExpiryMs = System.currentTimeMillis() + 60_000L
        prefs.clearOverride()
        assertEquals("", prefs.activeOverridePackage)
    }

    @Test
    fun clearOverride_resetsExpiryToZero() {
        prefs.activeOverrideExpiryMs = System.currentTimeMillis() + 60_000L
        prefs.clearOverride()
        assertEquals(0L, prefs.activeOverrideExpiryMs)
    }

    @Test
    fun clearOverride_calledOnCleanState_isIdempotent() {
        prefs.clearOverride()
        prefs.clearOverride()
        assertEquals("", prefs.activeOverridePackage)
        assertEquals(0L, prefs.activeOverrideExpiryMs)
    }

    // ── isOverrideActive ──────────────────────────────────────────────────────

    @Test
    fun isOverrideActive_noOverrideSet_returnsFalse() {
        assertFalse(prefs.isOverrideActive("com.any"))
    }

    @Test
    fun isOverrideActive_differentPackage_returnsFalse() {
        prefs.activeOverridePackage = "com.other"
        prefs.activeOverrideExpiryMs = System.currentTimeMillis() + 60_000L
        assertFalse(prefs.isOverrideActive("com.different"))
    }

    @Test
    fun isOverrideActive_matchingPackageAndFutureExpiry_returnsTrue() {
        val pkg = "com.focuslock.test"
        prefs.activeOverridePackage = pkg
        prefs.activeOverrideExpiryMs = System.currentTimeMillis() + 5 * 60_000L
        assertTrue(prefs.isOverrideActive(pkg))
    }

    @Test
    fun isOverrideActive_matchingPackageButExpired_returnsFalse() {
        val pkg = "com.focuslock.test"
        prefs.activeOverridePackage = pkg
        prefs.activeOverrideExpiryMs = System.currentTimeMillis() - 1L // already expired
        assertFalse(prefs.isOverrideActive(pkg))
    }

    @Test
    fun isOverrideActive_afterClearOverride_returnsFalse() {
        val pkg = "com.focuslock.test"
        prefs.activeOverridePackage = pkg
        prefs.activeOverrideExpiryMs = System.currentTimeMillis() + 60_000L
        prefs.clearOverride()
        assertFalse(prefs.isOverrideActive(pkg))
    }

    @Test
    fun isOverrideActive_expiryAtExactlyNow_returnsFalse() {
        val pkg = "com.edge"
        val now = System.currentTimeMillis()
        prefs.activeOverridePackage = pkg
        prefs.activeOverrideExpiryMs = now
        // System.currentTimeMillis() in isOverrideActive will be >= now → false
        assertFalse(prefs.isOverrideActive(pkg))
    }
}
