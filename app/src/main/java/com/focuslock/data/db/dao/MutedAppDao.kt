package com.focuslock.data.db.dao

import androidx.room.*
import com.focuslock.data.db.entities.MutedApp

@Dao
interface MutedAppDao {

    @Query("SELECT * FROM muted_apps WHERE muteRuleId = :ruleId")
    suspend fun getAppsForRule(ruleId: Long): List<MutedApp>

    @Query("SELECT packageName FROM muted_apps WHERE muteRuleId IN (:ruleIds)")
    suspend fun getPackagesForRules(ruleIds: List<Long>): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<MutedApp>)

    @Query("DELETE FROM muted_apps WHERE muteRuleId = :ruleId")
    suspend fun deleteAllForRule(ruleId: Long)
}
