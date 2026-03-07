package com.focuslock

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the time-window evaluation algorithm used in ProfileRepository.isTimeInWindow().
 *
 * The algorithm is reproduced here (pure function, no Android deps) so it can be
 * exercised from the JVM test source set without needing a device or emulator.
 *
 * Mirrors ProfileRepository.kt lines 91–103.
 */
class ScheduleWindowTest {

    /** Replication of ProfileRepository.isTimeInWindow() for isolated testing. */
    private fun isTimeInWindow(
        hour: Int, minute: Int,
        startHour: Int, startMinute: Int,
        endHour: Int, endMinute: Int
    ): Boolean {
        val currentMins = hour * 60 + minute
        val startMins = startHour * 60 + startMinute
        val endMins = endHour * 60 + endMinute
        return if (endMins > startMins) {
            currentMins in startMins until endMins
        } else {
            // Overnight window: start > end (e.g. 22:00–07:00)
            currentMins >= startMins || currentMins < endMins
        }
    }

    // ── Normal windows (end > start) ─────────────────────────────────────────

    @Test
    fun normalWindow_exactlyAtStart_isInside() {
        assertTrue(isTimeInWindow(9, 0, 9, 0, 17, 0))
    }

    @Test
    fun normalWindow_oneMinuteBeforeEnd_isInside() {
        assertTrue(isTimeInWindow(16, 59, 9, 0, 17, 0))
    }

    @Test
    fun normalWindow_exactlyAtEnd_isOutside() {
        // Range is [start, end) — end is exclusive
        assertFalse(isTimeInWindow(17, 0, 9, 0, 17, 0))
    }

    @Test
    fun normalWindow_oneMinuteAfterEnd_isOutside() {
        assertFalse(isTimeInWindow(17, 1, 9, 0, 17, 0))
    }

    @Test
    fun normalWindow_beforeStart_isOutside() {
        assertFalse(isTimeInWindow(8, 59, 9, 0, 17, 0))
    }

    @Test
    fun normalWindow_midday_isInside() {
        assertTrue(isTimeInWindow(12, 30, 9, 0, 17, 0))
    }

    @Test
    fun normalWindow_midnight_isOutside() {
        assertFalse(isTimeInWindow(0, 0, 9, 0, 17, 0))
    }

    @Test
    fun normalWindow_withMinutes_startBoundary() {
        assertTrue(isTimeInWindow(8, 30, 8, 30, 12, 0))
    }

    @Test
    fun normalWindow_withMinutes_oneMinuteBefore() {
        assertFalse(isTimeInWindow(8, 29, 8, 30, 12, 0))
    }

    // ── Overnight windows (end < start) ──────────────────────────────────────

    @Test
    fun overnightWindow_exactlyAtStart_isInside() {
        assertTrue(isTimeInWindow(22, 0, 22, 0, 7, 0))
    }

    @Test
    fun overnightWindow_lateNight_isInside() {
        assertTrue(isTimeInWindow(23, 59, 22, 0, 7, 0))
    }

    @Test
    fun overnightWindow_midnight_isInside() {
        assertTrue(isTimeInWindow(0, 0, 22, 0, 7, 0))
    }

    @Test
    fun overnightWindow_earlyMorning_isInside() {
        assertTrue(isTimeInWindow(6, 59, 22, 0, 7, 0))
    }

    @Test
    fun overnightWindow_exactlyAtEnd_isOutside() {
        // End is exclusive
        assertFalse(isTimeInWindow(7, 0, 22, 0, 7, 0))
    }

    @Test
    fun overnightWindow_afterEnd_isOutside() {
        assertFalse(isTimeInWindow(7, 30, 22, 0, 7, 0))
    }

    @Test
    fun overnightWindow_justBeforeStart_isOutside() {
        assertFalse(isTimeInWindow(21, 59, 22, 0, 7, 0))
    }

    @Test
    fun overnightWindow_midday_isOutside() {
        assertFalse(isTimeInWindow(12, 0, 22, 0, 7, 0))
    }

    // ── Edge cases ────────────────────────────────────────────────────────────

    @Test
    fun edgeCase_fullDay_midnight_isInside() {
        // 00:00–23:59: normal window covering almost the whole day
        assertTrue(isTimeInWindow(0, 0, 0, 0, 23, 59))
        assertTrue(isTimeInWindow(12, 0, 0, 0, 23, 59))
        assertFalse(isTimeInWindow(23, 59, 0, 0, 23, 59)) // end is exclusive
    }

    @Test
    fun edgeCase_singleMinuteWindow_exactMatch() {
        assertTrue(isTimeInWindow(10, 30, 10, 30, 10, 31))
    }

    @Test
    fun edgeCase_singleMinuteWindow_outsideByOne() {
        assertFalse(isTimeInWindow(10, 31, 10, 30, 10, 31))
    }
}
