package com.focuslock.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mute_rules")
data class MuteRule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val isEnabled: Boolean = true,
    val scheduleType: MuteScheduleType = MuteScheduleType.DATE_RANGE,

    // DATE_RANGE: epoch-ms timestamps for start and end of the muted period
    val startDateMs: Long = System.currentTimeMillis(),
    val endDateMs: Long = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L,

    // WEEKDAYS / WEEKENDS / EVERY_DAY: optional hour window
    val useHourRange: Boolean = false,
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 18,
    val endMinute: Int = 0,

    val createdAt: Long = System.currentTimeMillis()
)
