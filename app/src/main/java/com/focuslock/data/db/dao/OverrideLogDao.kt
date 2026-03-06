package com.focuslock.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.focuslock.data.db.entities.OverrideLog

@Dao
interface OverrideLogDao {

    @Query("SELECT * FROM override_logs ORDER BY timestamp DESC")
    fun getAllLogs(): LiveData<List<OverrideLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: OverrideLog): Long

    @Query("SELECT COUNT(*) FROM override_logs WHERE packageName = :packageName")
    suspend fun countByPackage(packageName: String): Int

    @Query("DELETE FROM override_logs")
    suspend fun clearAll()
}
