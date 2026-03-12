package com.focuslock.data.db

import androidx.room.TypeConverter
import com.focuslock.data.db.entities.MuteScheduleType

class Converters {
    @TypeConverter
    fun fromMuteScheduleType(value: MuteScheduleType): String = value.name

    @TypeConverter
    fun toMuteScheduleType(value: String): MuteScheduleType =
        MuteScheduleType.valueOf(value)
}
