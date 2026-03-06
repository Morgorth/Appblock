package com.focuslock.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.focuslock.data.db.dao.BlockedAppDao
import com.focuslock.data.db.dao.OverrideLogDao
import com.focuslock.data.db.dao.ProfileDao
import com.focuslock.data.db.dao.ScheduleDayDao
import com.focuslock.data.db.entities.BlockedApp
import com.focuslock.data.db.entities.OverrideLog
import com.focuslock.data.db.entities.Profile
import com.focuslock.data.db.entities.ScheduleDay

@Database(
    entities = [Profile::class, BlockedApp::class, ScheduleDay::class, OverrideLog::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun blockedAppDao(): BlockedAppDao
    abstract fun scheduleDayDao(): ScheduleDayDao
    abstract fun overrideLogDao(): OverrideLogDao

    companion object {
        private const val DB_NAME = "focuslock.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    // IMPORTANT: before bumping `version`, replace this with
                    // .addMigrations(MIGRATION_1_2, ...) to preserve user data.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
