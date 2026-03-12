package com.focuslock.data.db.entities

enum class MuteScheduleType {
    DATE_RANGE,   // blocked between two specific calendar dates
    WEEKDAYS,     // Mon–Fri, with optional hour window
    WEEKENDS,     // Sat–Sun, with optional hour window
    EVERY_DAY     // all 7 days, with optional hour window
}
