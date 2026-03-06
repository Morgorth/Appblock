package com.focuslock.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    fun formatTime(hour: Int, minute: Int): String =
        String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

    fun formatDateTime(epochMs: Long): String =
        dateTimeFormat.format(Date(epochMs))

    fun dayName(dayOfWeek: Int): String = when (dayOfWeek) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        7 -> "Sunday"
        else -> "Unknown"
    }

    fun dayNameShort(dayOfWeek: Int): String = dayName(dayOfWeek).take(3)

    /** Returns a human-readable schedule summary, e.g. "Mon–Fri, 09:00–17:00" */
    fun scheduleSummary(
        enabledDays: List<Int>, // 1=Mon..7=Sun
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int
    ): String {
        if (enabledDays.isEmpty()) return "No days scheduled"
        val dayStr = when {
            enabledDays == listOf(1, 2, 3, 4, 5) -> "Weekdays"
            enabledDays == listOf(6, 7) -> "Weekends"
            enabledDays == (1..7).toList() -> "Every day"
            else -> enabledDays.joinToString(", ") { dayNameShort(it) }
        }
        return "$dayStr, ${formatTime(startHour, startMinute)}–${formatTime(endHour, endMinute)}"
    }
}
