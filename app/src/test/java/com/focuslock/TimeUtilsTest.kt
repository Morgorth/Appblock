package com.focuslock

import com.focuslock.utils.TimeUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeUtilsTest {

    // ── formatTime ────────────────────────────────────────────────────────────

    @Test
    fun formatTime_zeroHourAndMinute() {
        assertEquals("00:00", TimeUtils.formatTime(0, 0))
    }

    @Test
    fun formatTime_singleDigitsPaddedWithZero() {
        assertEquals("09:05", TimeUtils.formatTime(9, 5))
    }

    @Test
    fun formatTime_endOfDay() {
        assertEquals("23:59", TimeUtils.formatTime(23, 59))
    }

    @Test
    fun formatTime_noon() {
        assertEquals("12:30", TimeUtils.formatTime(12, 30))
    }

    // ── dayName ───────────────────────────────────────────────────────────────

    @Test
    fun dayName_monday() = assertEquals("Monday", TimeUtils.dayName(1))

    @Test
    fun dayName_tuesday() = assertEquals("Tuesday", TimeUtils.dayName(2))

    @Test
    fun dayName_wednesday() = assertEquals("Wednesday", TimeUtils.dayName(3))

    @Test
    fun dayName_thursday() = assertEquals("Thursday", TimeUtils.dayName(4))

    @Test
    fun dayName_friday() = assertEquals("Friday", TimeUtils.dayName(5))

    @Test
    fun dayName_saturday() = assertEquals("Saturday", TimeUtils.dayName(6))

    @Test
    fun dayName_sunday() = assertEquals("Sunday", TimeUtils.dayName(7))

    @Test
    fun dayName_invalidReturnsUnknown() = assertEquals("Unknown", TimeUtils.dayName(0))

    @Test
    fun dayName_negativeReturnsUnknown() = assertEquals("Unknown", TimeUtils.dayName(-1))

    @Test
    fun dayName_outOfRangeHighReturnsUnknown() = assertEquals("Unknown", TimeUtils.dayName(8))

    // ── dayNameShort ──────────────────────────────────────────────────────────

    @Test
    fun dayNameShort_mondayIsThreeChars() = assertEquals("Mon", TimeUtils.dayNameShort(1))

    @Test
    fun dayNameShort_fridayIsThreeChars() = assertEquals("Fri", TimeUtils.dayNameShort(5))

    @Test
    fun dayNameShort_sundayIsThreeChars() = assertEquals("Sun", TimeUtils.dayNameShort(7))

    @Test
    fun dayNameShort_invalidIsUnk() = assertEquals("Unk", TimeUtils.dayNameShort(99))

    // ── scheduleSummary ───────────────────────────────────────────────────────

    @Test
    fun scheduleSummary_noDaysReturnsNoScheduled() {
        assertEquals(
            "No days scheduled",
            TimeUtils.scheduleSummary(emptyList(), 9, 0, 17, 0)
        )
    }

    @Test
    fun scheduleSummary_weekdays() {
        val result = TimeUtils.scheduleSummary(listOf(1, 2, 3, 4, 5), 9, 0, 17, 0)
        assertEquals("Weekdays, 09:00–17:00", result)
    }

    @Test
    fun scheduleSummary_weekends() {
        val result = TimeUtils.scheduleSummary(listOf(6, 7), 10, 0, 18, 0)
        assertEquals("Weekends, 10:00–18:00", result)
    }

    @Test
    fun scheduleSummary_everyDay() {
        val result = TimeUtils.scheduleSummary((1..7).toList(), 0, 0, 23, 59)
        assertEquals("Every day, 00:00–23:59", result)
    }

    @Test
    fun scheduleSummary_customDaysList() {
        // Mon + Wed + Fri (not a preset)
        val result = TimeUtils.scheduleSummary(listOf(1, 3, 5), 8, 30, 12, 0)
        assertEquals("Mon, Wed, Fri, 08:30–12:00", result)
    }

    @Test
    fun scheduleSummary_singleDay() {
        val result = TimeUtils.scheduleSummary(listOf(3), 22, 0, 6, 0)
        assertEquals("Wed, 22:00–06:00", result)
    }

    @Test
    fun scheduleSummary_timePartsArePadded() {
        val result = TimeUtils.scheduleSummary(listOf(1, 2, 3, 4, 5), 9, 5, 17, 1)
        assertEquals("Weekdays, 09:05–17:01", result)
    }
}
