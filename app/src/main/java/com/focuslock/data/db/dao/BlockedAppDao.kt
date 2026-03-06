package com.focuslock.data.db.dao

import androidx.room.*
import com.focuslock.data.db.entities.BlockedApp

@Dao
interface BlockedAppDao {

    @Query("SELECT * FROM blocked_apps WHERE profileId = :profileId")
    suspend fun getBlockedAppsForProfile(profileId: Long): List<BlockedApp>

    @Query("SELECT DISTINCT packageName FROM blocked_apps WHERE profileId IN (:profileIds)")
    suspend fun getBlockedPackagesForProfiles(profileIds: List<Long>): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blockedApp: BlockedApp): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<BlockedApp>)

    @Delete
    suspend fun delete(blockedApp: BlockedApp)

    @Query("DELETE FROM blocked_apps WHERE profileId = :profileId")
    suspend fun deleteAllForProfile(profileId: Long)
}
