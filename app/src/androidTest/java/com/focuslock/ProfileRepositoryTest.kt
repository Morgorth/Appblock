package com.focuslock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.focuslock.data.db.AppDatabase
import com.focuslock.data.db.entities.BlockedApp
import com.focuslock.data.db.entities.ScheduleDay
import com.focuslock.data.repository.ProfileRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class ProfileRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var repo: ProfileRepository

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repo = ProfileRepository(db.profileDao(), db.blockedAppDao(), db.scheduleDayDao())
    }

    @After
    fun tearDown() = db.close()

    // ── createProfile ─────────────────────────────────────────────────────────

    @Test
    fun createProfile_returnsNonZeroId() = runTest {
        val id = repo.createProfile("Work")
        assertTrue(id > 0)
    }

    @Test
    fun createProfile_createsSevenDefaultScheduleDays() = runTest {
        val id = repo.createProfile("Study")
        val schedule = repo.getSchedule(id)
        assertEquals(7, schedule.size)
        assertEquals((1..7).toList(), schedule.map { it.dayOfWeek }.sorted())
    }

    @Test
    fun createProfile_defaultScheduleDaysAreDisabled() = runTest {
        val id = repo.createProfile("Evening")
        repo.getSchedule(id).forEach { day ->
            assertFalse("Day ${day.dayOfWeek} should be disabled by default", day.isEnabled)
        }
    }

    @Test
    fun createProfile_customOverrideDuration() = runTest {
        val id = repo.createProfile("Strict", overrideDurationMinutes = 15)
        val profile = repo.getProfileById(id)
        assertEquals(15, profile!!.overrideDurationMinutes)
    }

    // ── getProfileById ────────────────────────────────────────────────────────

    @Test
    fun getProfileById_returnsNull_forUnknownId() = runTest {
        assertNull(repo.getProfileById(9999L))
    }

    // ── updateProfile ─────────────────────────────────────────────────────────

    @Test
    fun updateProfile_changesName() = runTest {
        val id = repo.createProfile("Old")
        val profile = repo.getProfileById(id)!!
        repo.updateProfile(profile.copy(name = "New"))
        assertEquals("New", repo.getProfileById(id)!!.name)
    }

    // ── setProfileEnabled ─────────────────────────────────────────────────────

    @Test
    fun setProfileEnabled_false_disablesProfile() = runTest {
        val id = repo.createProfile("Toggle")
        repo.setProfileEnabled(id, false)
        assertFalse(repo.getProfileById(id)!!.isEnabled)
    }

    // ── deleteProfile ─────────────────────────────────────────────────────────

    @Test
    fun deleteProfile_profileNoLongerExists() = runTest {
        val id = repo.createProfile("ToDelete")
        repo.deleteProfile(repo.getProfileById(id)!!)
        assertNull(repo.getProfileById(id))
    }

    @Test
    fun deleteProfile_cascadesScheduleDays() = runTest {
        val id = repo.createProfile("WithSchedule")
        repo.deleteProfile(repo.getProfileById(id)!!)
        assertTrue(repo.getSchedule(id).isEmpty())
    }

    // ── saveBlockedApps ───────────────────────────────────────────────────────

    @Test
    fun saveBlockedApps_replacesExistingList() = runTest {
        val id = repo.createProfile("Apps")
        repo.saveBlockedApps(id, listOf(
            BlockedApp(profileId = id, packageName = "com.old", appName = "Old")
        ))
        repo.saveBlockedApps(id, listOf(
            BlockedApp(profileId = id, packageName = "com.new1", appName = "New1"),
            BlockedApp(profileId = id, packageName = "com.new2", appName = "New2")
        ))
        val apps = repo.getBlockedApps(id)
        assertEquals(2, apps.size)
        assertTrue(apps.none { it.packageName == "com.old" })
    }

    @Test
    fun saveBlockedApps_emptyList_clearsApps() = runTest {
        val id = repo.createProfile("Clear")
        repo.saveBlockedApps(id, listOf(
            BlockedApp(profileId = id, packageName = "com.x", appName = "X")
        ))
        repo.saveBlockedApps(id, emptyList())
        assertTrue(repo.getBlockedApps(id).isEmpty())
    }

    // ── getCurrentlyBlockedPackages ───────────────────────────────────────────

    @Test
    fun getCurrentlyBlockedPackages_noProfiles_returnsEmpty() = runTest {
        val blocked = repo.getCurrentlyBlockedPackages()
        assertTrue(blocked.isEmpty())
    }

    @Test
    fun getCurrentlyBlockedPackages_profileDisabled_returnsEmpty() = runTest {
        val id = repo.createProfile("Disabled")
        repo.setProfileEnabled(id, false)
        val blocked = repo.getCurrentlyBlockedPackages()
        assertTrue(blocked.isEmpty())
    }

    @Test
    fun getCurrentlyBlockedPackages_noScheduleEnabled_returnsEmpty() = runTest {
        val id = repo.createProfile("NoSchedule")
        // Default schedule: all days disabled — nothing active
        repo.saveBlockedApps(id, listOf(
            BlockedApp(profileId = id, packageName = "com.shouldNotBlock", appName = "App")
        ))
        val blocked = repo.getCurrentlyBlockedPackages()
        assertTrue(blocked.isEmpty())
    }

    @Test
    fun getCurrentlyBlockedPackages_activeScheduleAndTime_returnsBlockedPackages() = runTest {
        val id = repo.createProfile("Active")

        // Enable today with a window of 00:00–23:59 (covers any time this test runs)
        val todayDow = todayDayOfWeek()
        val schedule = repo.getSchedule(id).map { day ->
            if (day.dayOfWeek == todayDow)
                day.copy(isEnabled = true, startHour = 0, startMinute = 0, endHour = 23, endMinute = 59)
            else day
        }
        repo.saveSchedule(id, schedule)

        repo.saveBlockedApps(id, listOf(
            BlockedApp(profileId = id, packageName = "com.blocked", appName = "Blocked App")
        ))

        val blocked = repo.getCurrentlyBlockedPackages()
        assertTrue(blocked.contains("com.blocked"))
    }

    @Test
    fun getCurrentlyBlockedPackages_scheduleNotActiveToday_returnsEmpty() = runTest {
        val id = repo.createProfile("WrongDay")
        val todayDow = todayDayOfWeek()
        // Enable a different day's schedule only
        val otherDay = if (todayDow == 1) 2 else 1
        val schedule = repo.getSchedule(id).map { day ->
            if (day.dayOfWeek == otherDay)
                day.copy(isEnabled = true, startHour = 0, startMinute = 0, endHour = 23, endMinute = 59)
            else day
        }
        repo.saveSchedule(id, schedule)

        repo.saveBlockedApps(id, listOf(
            BlockedApp(profileId = id, packageName = "com.pkg", appName = "App")
        ))

        val blocked = repo.getCurrentlyBlockedPackages()
        assertTrue(blocked.isEmpty())
    }

    // ── saveSchedule ──────────────────────────────────────────────────────────

    @Test
    fun saveSchedule_replacesExistingEntries() = runTest {
        val id = repo.createProfile("Sched")
        val original = repo.getSchedule(id)

        val updated = original.map { it.copy(isEnabled = true, startHour = 8) }
        repo.saveSchedule(id, updated)

        val fetched = repo.getSchedule(id)
        assertTrue(fetched.all { it.isEnabled })
        assertTrue(fetched.all { it.startHour == 8 })
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    /**
     * Returns today's day-of-week using FocusLock's convention (1=Mon, 7=Sun).
     */
    private fun todayDayOfWeek(): Int {
        val androidDow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        return if (androidDow == Calendar.SUNDAY) 7 else androidDow - 1
    }
}
