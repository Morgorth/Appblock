package com.focuslock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.focuslock.data.db.AppDatabase
import com.focuslock.data.db.dao.ProfileDao
import com.focuslock.data.db.entities.Profile
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: ProfileDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.profileDao()
    }

    @After
    fun tearDown() = db.close()

    // ── Insert & retrieve ─────────────────────────────────────────────────────

    @Test
    fun insert_returnsGeneratedId() = runTest {
        val id = dao.insert(Profile(name = "Work"))
        assertTrue(id > 0)
    }

    @Test
    fun getProfileById_returnsInsertedProfile() = runTest {
        val id = dao.insert(Profile(name = "Focus"))
        val profile = dao.getProfileById(id)
        assertNotNull(profile)
        assertEquals("Focus", profile!!.name)
    }

    @Test
    fun getProfileById_unknownId_returnsNull() = runTest {
        val profile = dao.getProfileById(999L)
        assertNull(profile)
    }

    @Test
    fun getAllProfilesSync_returnsAllInserted() = runTest {
        dao.insert(Profile(name = "A"))
        dao.insert(Profile(name = "B"))
        dao.insert(Profile(name = "C"))
        val all = dao.getAllProfilesSync()
        assertEquals(3, all.size)
    }

    @Test
    fun getAllProfilesSync_emptyTable_returnsEmptyList() = runTest {
        val all = dao.getAllProfilesSync()
        assertTrue(all.isEmpty())
    }

    // ── Enabled flag ──────────────────────────────────────────────────────────

    @Test
    fun getEnabledProfiles_excludesDisabledOnes() = runTest {
        val enabledId = dao.insert(Profile(name = "Enabled", isEnabled = true))
        dao.insert(Profile(name = "Disabled", isEnabled = false))
        val enabled = dao.getEnabledProfiles()
        assertEquals(1, enabled.size)
        assertEquals(enabledId, enabled[0].id)
    }

    @Test
    fun setEnabled_togglesFlag() = runTest {
        val id = dao.insert(Profile(name = "Toggle", isEnabled = true))
        dao.setEnabled(id, false)
        val profile = dao.getProfileById(id)
        assertFalse(profile!!.isEnabled)
    }

    @Test
    fun setEnabled_enablesDisabledProfile() = runTest {
        val id = dao.insert(Profile(name = "Dormant", isEnabled = false))
        dao.setEnabled(id, true)
        val enabled = dao.getEnabledProfiles()
        assertEquals(1, enabled.size)
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Test
    fun update_changesProfileName() = runTest {
        val id = dao.insert(Profile(name = "Old Name"))
        val profile = dao.getProfileById(id)!!
        dao.update(profile.copy(name = "New Name"))
        assertEquals("New Name", dao.getProfileById(id)!!.name)
    }

    @Test
    fun update_changesOverrideDuration() = runTest {
        val id = dao.insert(Profile(name = "Prof", overrideDurationMinutes = 5))
        val profile = dao.getProfileById(id)!!
        dao.update(profile.copy(overrideDurationMinutes = 15))
        assertEquals(15, dao.getProfileById(id)!!.overrideDurationMinutes)
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Test
    fun delete_removesProfile() = runTest {
        val id = dao.insert(Profile(name = "ToDelete"))
        val profile = dao.getProfileById(id)!!
        dao.delete(profile)
        assertNull(dao.getProfileById(id))
    }

    @Test
    fun delete_leavesOtherProfilesIntact() = runTest {
        val id1 = dao.insert(Profile(name = "Keep"))
        val id2 = dao.insert(Profile(name = "Remove"))
        dao.delete(dao.getProfileById(id2)!!)
        assertNotNull(dao.getProfileById(id1))
        assertNull(dao.getProfileById(id2))
    }

    // ── Default values ────────────────────────────────────────────────────────

    @Test
    fun insert_defaultsEnabledTrue() = runTest {
        val id = dao.insert(Profile(name = "Default"))
        assertTrue(dao.getProfileById(id)!!.isEnabled)
    }

    @Test
    fun insert_defaultOverrideDurationIsFive() = runTest {
        val id = dao.insert(Profile(name = "Default"))
        assertEquals(5, dao.getProfileById(id)!!.overrideDurationMinutes)
    }
}
