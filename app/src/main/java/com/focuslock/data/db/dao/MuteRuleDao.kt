package com.focuslock.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.focuslock.data.db.entities.MuteRule

@Dao
interface MuteRuleDao {

    @Query("SELECT * FROM mute_rules ORDER BY createdAt ASC")
    fun getAllRules(): LiveData<List<MuteRule>>

    @Query("SELECT * FROM mute_rules WHERE isEnabled = 1")
    suspend fun getEnabledRulesSync(): List<MuteRule>

    @Query("SELECT * FROM mute_rules WHERE id = :id")
    suspend fun getRuleById(id: Long): MuteRule?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: MuteRule): Long

    @Update
    suspend fun update(rule: MuteRule)

    @Delete
    suspend fun delete(rule: MuteRule)

    @Query("UPDATE mute_rules SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun setEnabled(id: Long, isEnabled: Boolean)
}
