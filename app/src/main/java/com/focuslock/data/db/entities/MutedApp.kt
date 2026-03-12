package com.focuslock.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "muted_apps",
    foreignKeys = [
        ForeignKey(
            entity = MuteRule::class,
            parentColumns = ["id"],
            childColumns = ["muteRuleId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("muteRuleId")]
)
data class MutedApp(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val muteRuleId: Long,
    val packageName: String,
    val appName: String
)
