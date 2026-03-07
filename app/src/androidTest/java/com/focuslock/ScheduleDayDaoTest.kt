package com.focuslock

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.focuslock.data.db.AppDatabase
import com.focuslock.data.db.dao.ProfileDao
import com.focuslock.data.db.dao.ScheduleDayDao
import com.focuslock.data.db.entities.Profile
import com.focuslock.data.db.entities.ScheduleDay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScheduleDayDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var profileDao: ProfileDao
    private lateinit var dao: ScheduleDayDao
    private var profileId = 0L

    @Before
    fun setUp() = runTest {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        profileDao = db.profileDao()
        dao = db.scheduleDayDao()
        profileId = profileDao.insert(Profile(name = "Test Profile"))
    }

    @After
    fun tearDown() = db.close()

    private fun day(
        day: Int,
        enabled: Boolean = false,
        startH: Int = 9, startM: Int = 0,
        endH: Int = 17, endM: Int = 0
    ) = ScheduleDay(
        profileId = profileId,
        dayOfWeek = day,
        isEnabled = enabled,
        startHour = startH, startMinute = startM,
        endHour = endH, endMinute = endM
    )

    // ── Insert & retrieve ─────────────────────────────────────────────────────

    @Test
    fun insert_returnsPositiveId() = runTest {
        val id = dao.insert(day(1))
        assertTrue(id > 0)
    }

    @Test
    fun getScheduleForProfile_returnsOrderedByDayOfWeek() = runTest {
        // Insert out of order
        dao.insertAll(listOf(day(5), day(3), day(1), day(7), day(2), day(4), day(6)))
        val schedule = dao.getScheduleForProfile(profileId)
        assertEquals(7, schedule.size)
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7), schedule.map { it.dayOfWeek })
    }

    @Test
    fun getScheduleForProfile_emptyResult_noEntries() = runTest {
        val schedule = dao.getScheduleForProfile(profileId)
        assertTrue(schedule.isEmpty())
    }

    @Test
    fun getScheduleForProfile_onlyReturnsOwnProfile() = runTest {
        val otherId = profileDao.insert(Profile(name = "Other"))
        dao.insert(ScheduleDay(profileId = otherId, dayOfWeek = 1))
        dao.insert(day(2))

        val schedule = dao.getScheduleForProfile(profileId)
        assertEquals(1, schedule.size)
        assertEquals(2, schedule[0].dayOfWeek)
    }

    // ── insertAll ─────────────────────────────────────────────────────────────

    @Test
    fun insertAll_insertsAllSevenDays() = runTest {
        val allDays = (1..7).map { day(it) }
        dao.insertAll(allDays)
        assertEquals(7, dao.getScheduleForProfile(profileId).size)
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Test
    fun update_changesEnabledFlag() = runTest {
        val id = dao.insert(day(1, enabled = false))
        val entry = dao.getScheduleForProfile(profileId).first()
        dao.update(entry.copy(isEnabled = true))

        val updated = dao.getScheduleForProfile(profileId).first()
        assertTrue(updated.isEnabled)
    }

    @Test
    fun update_changesTimeWindow() = runTest {
        dao.insert(day(1, startH = 9, startM = 0, endH = 17, endM = 0))
        val entry = dao.getScheduleForProfile(profileId).first()
        dao.update(entry.copy(startHour = 22, startMinute = 0, endHour = 7, endMinute = 0))

        val updated = dao.getScheduleForProfile(profileId).first()
        assertEquals(22, updated.startHour)
        assertEquals(7, updated.endHour)
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Test
    fun delete_removesSpecificDay() = runTest {
        dao.insertAll(listOf(day(1), day(2), day(3)))
        val monday = dao.getScheduleForProfile(profileId).first { it.dayOfWeek == 1 }
        dao.delete(monday)

        val remaining = dao.getScheduleForProfile(profileId)
        assertEquals(2, remaining.size)
        assertTrue(remaining.none { it.dayOfWeek == 1 })
    }

    @Test
    fun deleteAllForProfile_removesAllDays() = runTest {
        dao.insertAll((1..7).map { day(it) })
        dao.deleteAllForProfile(profileId)
        assertTrue(dao.getScheduleForProfile(profileId).isEmpty())
    }

    @Test
    fun deleteAllForProfile_leavesOtherProfilesSchedule() = runTest {
        val otherId = profileDao.insert(Profile(name = "Other"))
        dao.insert(ScheduleDay(profileId = otherId, dayOfWeek = 3))
        dao.insertAll((1..7).map { day(it) })

        dao.deleteAllForProfile(profileId)

        val otherSchedule = dao.getScheduleForProfile(otherId)
        assertEquals(1, otherSchedule.size)
    }

    @Test
    fun cascade_deletingProfileDeletesItsSchedule() = runTest {
        dao.insertAll((1..7).map { day(it) })
        profileDao.delete(profileDao.getProfileById(profileId)!!)
        assertTrue(dao.getScheduleForProfile(profileId).isEmpty())
    }

    // ── Default values ────────────────────────────────────────────────────────

    @Test
    fun defaults_disabledWith9to17Window() = runTest {
        dao.insert(ScheduleDay(profileId = profileId, dayOfWeek = 1))
        val entry = dao.getScheduleForProfile(profileId).first()
        assertFalse(entry.isEnabled)
        assertEquals(9, entry.startHour)
        assertEquals(0, entry.startMinute)
        assertEquals(17, entry.endHour)
        assertEquals(0, entry.endMinute)
    }
}
