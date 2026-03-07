package com.focuslock

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.focuslock.data.db.AppDatabase
import com.focuslock.data.db.dao.BlockedAppDao
import com.focuslock.data.db.dao.ProfileDao
import com.focuslock.data.db.entities.BlockedApp
import com.focuslock.data.db.entities.Profile
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BlockedAppDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var profileDao: ProfileDao
    private lateinit var dao: BlockedAppDao

    private var profileId1 = 0L
    private var profileId2 = 0L

    @Before
    fun setUp() = runTest {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        profileDao = db.profileDao()
        dao = db.blockedAppDao()

        profileId1 = profileDao.insert(Profile(name = "Profile 1"))
        profileId2 = profileDao.insert(Profile(name = "Profile 2"))
    }

    @After
    fun tearDown() = db.close()

    private fun app(profileId: Long, pkg: String, name: String = pkg) =
        BlockedApp(profileId = profileId, packageName = pkg, appName = name)

    // ── Insert & fetch ────────────────────────────────────────────────────────

    @Test
    fun insert_returnsGeneratedId() = runTest {
        val id = dao.insert(app(profileId1, "com.example.app"))
        assertTrue(id > 0)
    }

    @Test
    fun getBlockedAppsForProfile_returnsOnlyThatProfilesApps() = runTest {
        dao.insert(app(profileId1, "com.a", "App A"))
        dao.insert(app(profileId1, "com.b", "App B"))
        dao.insert(app(profileId2, "com.c", "App C"))

        val apps1 = dao.getBlockedAppsForProfile(profileId1)
        assertEquals(2, apps1.size)
        assertTrue(apps1.all { it.profileId == profileId1 })
    }

    @Test
    fun getBlockedAppsForProfile_noApps_returnsEmpty() = runTest {
        val apps = dao.getBlockedAppsForProfile(profileId1)
        assertTrue(apps.isEmpty())
    }

    // ── insertAll ─────────────────────────────────────────────────────────────

    @Test
    fun insertAll_insertsMultipleApps() = runTest {
        val batch = listOf(
            app(profileId1, "com.x"),
            app(profileId1, "com.y"),
            app(profileId1, "com.z")
        )
        dao.insertAll(batch)
        assertEquals(3, dao.getBlockedAppsForProfile(profileId1).size)
    }

    @Test
    fun insertAll_emptyList_doesNothing() = runTest {
        dao.insertAll(emptyList())
        assertTrue(dao.getBlockedAppsForProfile(profileId1).isEmpty())
    }

    // ── getBlockedPackagesForProfiles ─────────────────────────────────────────

    @Test
    fun getBlockedPackagesForProfiles_aggregatesAcrossProfiles() = runTest {
        dao.insert(app(profileId1, "com.shared"))
        dao.insert(app(profileId1, "com.only1"))
        dao.insert(app(profileId2, "com.shared"))
        dao.insert(app(profileId2, "com.only2"))

        val packages = dao.getBlockedPackagesForProfiles(listOf(profileId1, profileId2))
        // DISTINCT — shared package appears once
        assertEquals(3, packages.size)
        assertTrue(packages.containsAll(listOf("com.shared", "com.only1", "com.only2")))
    }

    @Test
    fun getBlockedPackagesForProfiles_singleProfile_returnsItsPackages() = runTest {
        dao.insert(app(profileId1, "com.alpha"))
        dao.insert(app(profileId1, "com.beta"))
        dao.insert(app(profileId2, "com.gamma")) // not queried

        val packages = dao.getBlockedPackagesForProfiles(listOf(profileId1))
        assertEquals(2, packages.size)
        assertFalse(packages.contains("com.gamma"))
    }

    @Test
    fun getBlockedPackagesForProfiles_emptyIdList_returnsEmpty() = runTest {
        dao.insert(app(profileId1, "com.something"))
        val packages = dao.getBlockedPackagesForProfiles(emptyList())
        assertTrue(packages.isEmpty())
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Test
    fun delete_removesSpecificEntry() = runTest {
        val id = dao.insert(app(profileId1, "com.toDelete"))
        dao.insert(app(profileId1, "com.keep"))

        val toDelete = dao.getBlockedAppsForProfile(profileId1).first { it.packageName == "com.toDelete" }
        dao.delete(toDelete)

        val remaining = dao.getBlockedAppsForProfile(profileId1)
        assertEquals(1, remaining.size)
        assertEquals("com.keep", remaining[0].packageName)
    }

    @Test
    fun deleteAllForProfile_clearsOnlyThatProfile() = runTest {
        dao.insert(app(profileId1, "com.a"))
        dao.insert(app(profileId1, "com.b"))
        dao.insert(app(profileId2, "com.c"))

        dao.deleteAllForProfile(profileId1)

        assertTrue(dao.getBlockedAppsForProfile(profileId1).isEmpty())
        assertEquals(1, dao.getBlockedAppsForProfile(profileId2).size)
    }

    @Test
    fun cascade_deletingProfileDeletesItsApps() = runTest {
        dao.insert(app(profileId1, "com.willBeGone"))
        val profile1 = profileDao.getProfileById(profileId1)!!
        profileDao.delete(profile1)

        val apps = dao.getBlockedAppsForProfile(profileId1)
        assertTrue(apps.isEmpty())
    }

    // ── Replace conflict strategy ─────────────────────────────────────────────

    @Test
    fun insert_replaceOnConflict_updatesExistingRow() = runTest {
        val id = dao.insert(app(profileId1, "com.pkg", "Old Name"))
        dao.insert(BlockedApp(id = id, profileId = profileId1, packageName = "com.pkg", appName = "New Name"))

        val apps = dao.getBlockedAppsForProfile(profileId1)
        assertEquals(1, apps.size)
        assertEquals("New Name", apps[0].appName)
    }
}
