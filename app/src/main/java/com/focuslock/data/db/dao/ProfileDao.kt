package com.focuslock.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.focuslock.data.db.entities.Profile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profiles ORDER BY createdAt ASC")
    fun getAllProfiles(): LiveData<List<Profile>>

    @Query("SELECT * FROM profiles ORDER BY createdAt ASC")
    suspend fun getAllProfilesSync(): List<Profile>

    @Query("SELECT * FROM profiles WHERE isEnabled = 1")
    suspend fun getEnabledProfiles(): List<Profile>

    @Query("SELECT * FROM profiles WHERE id = :id")
    suspend fun getProfileById(id: Long): Profile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Profile): Long

    @Update
    suspend fun update(profile: Profile)

    @Delete
    suspend fun delete(profile: Profile)

    @Query("UPDATE profiles SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun setEnabled(id: Long, isEnabled: Boolean)
}
