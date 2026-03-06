package com.focuslock.data.db.dao

import androidx.room.*
import com.focuslock.data.db.entities.ScheduleDay

@Dao
interface ScheduleDayDao {

    @Query("SELECT * FROM schedule_days WHERE profileId = :profileId ORDER BY dayOfWeek ASC")
    suspend fun getScheduleForProfile(profileId: Long): List<ScheduleDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scheduleDay: ScheduleDay): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(days: List<ScheduleDay>)

    @Update
    suspend fun update(scheduleDay: ScheduleDay)

    @Delete
    suspend fun delete(scheduleDay: ScheduleDay)

    @Query("DELETE FROM schedule_days WHERE profileId = :profileId")
    suspend fun deleteAllForProfile(profileId: Long)
}
