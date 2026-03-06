package com.focuslock.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "override_logs")
data class OverrideLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    val profileName: String,
    val justification: String,
    val timestamp: Long = System.currentTimeMillis(),
    val durationMinutes: Int
)
