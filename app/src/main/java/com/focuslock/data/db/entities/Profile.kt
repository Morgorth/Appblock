package com.focuslock.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val isEnabled: Boolean = true,
    val overrideDurationMinutes: Int = 5,
    val createdAt: Long = System.currentTimeMillis()
)
