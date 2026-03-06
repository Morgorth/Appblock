package com.focuslock.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a single day's schedule within a profile.
 * dayOfWeek: 1=Monday, 2=Tuesday, ..., 7=Sunday
 * Overnight schedules (e.g. 22:00–07:00) are supported: endHour < startHour means next day.
 */
@Entity(
    tableName = "schedule_days",
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId")]
)
data class ScheduleDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val dayOfWeek: Int, // 1=Mon .. 7=Sun
    val isEnabled: Boolean = false,
    val startHour: Int = 9,
    val startMinute: Int = 0,
    val endHour: Int = 17,
    val endMinute: Int = 0
)
