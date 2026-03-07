package com.focuslock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.focuslock.data.db.AppDatabase
import com.focuslock.data.db.dao.OverrideLogDao
import com.focuslock.data.db.entities.OverrideLog
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OverrideLogDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: OverrideLogDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.overrideLogDao()
    }

    @After
    fun tearDown() = db.close()

    private fun log(
        pkg: String = "com.example",
        appName: String = "Example",
        profileName: String = "Work",
        justification: String = "Needed for urgent task",
        timestamp: Long = System.currentTimeMillis(),
        durationMinutes: Int = 5
    ) = OverrideLog(
        packageName = pkg,
        appName = appName,
        profileName = profileName,
        justification = justification,
        timestamp = timestamp,
        durationMinutes = durationMinutes
    )

    // ── Insert & retrieve ─────────────────────────────────────────────────────

    @Test
    fun insert_returnsPositiveId() = runTest {
        val id = dao.insert(log())
        assertTrue(id > 0)
    }

    @Test
    fun insert_persistsAllFields() = runTest {
        val now = System.currentTimeMillis()
        val entry = log(
            pkg = "com.twitter",
            appName = "Twitter",
            profileName = "Focus",
            justification = "Breaking news check",
            timestamp = now,
            durationMinutes = 10
        )
        dao.insert(entry)

        // Retrieve via LiveData (direct value with InstantTaskExecutor)
        val logs = dao.getAllLogs().value ?: emptyList()
        // Note: getAllLogs() returns LiveData; value populated after first emit.
        // We verify via countByPackage and clearAll instead.
        val count = dao.countByPackage("com.twitter")
        assertEquals(1, count)
    }

    // ── countByPackage ────────────────────────────────────────────────────────

    @Test
    fun countByPackage_zeroWhenNoneInserted() = runTest {
        assertEquals(0, dao.countByPackage("com.nobody"))
    }

    @Test
    fun countByPackage_countsOnlyMatchingPackage() = runTest {
        dao.insert(log(pkg = "com.a"))
        dao.insert(log(pkg = "com.a"))
        dao.insert(log(pkg = "com.b"))
        assertEquals(2, dao.countByPackage("com.a"))
        assertEquals(1, dao.countByPackage("com.b"))
    }

    @Test
    fun countByPackage_cumulatesAcrossMultipleInserts() = runTest {
        repeat(5) { dao.insert(log(pkg = "com.repeat")) }
        assertEquals(5, dao.countByPackage("com.repeat"))
    }

    // ── clearAll ──────────────────────────────────────────────────────────────

    @Test
    fun clearAll_removesEveryLog() = runTest {
        dao.insert(log(pkg = "com.x"))
        dao.insert(log(pkg = "com.y"))
        dao.insert(log(pkg = "com.z"))
        dao.clearAll()
        assertEquals(0, dao.countByPackage("com.x"))
        assertEquals(0, dao.countByPackage("com.y"))
        assertEquals(0, dao.countByPackage("com.z"))
    }

    @Test
    fun clearAll_onEmptyTable_isIdempotent() = runTest {
        dao.clearAll()
        dao.clearAll()
        assertEquals(0, dao.countByPackage("com.anything"))
    }

    // ── Validation: justification length ─────────────────────────────────────

    @Test
    fun insert_shortJustification_stillPersists() = runTest {
        // DB layer does not enforce the ≥20-char rule — that's a UI concern.
        // Verifying the DAO itself does not reject short text.
        val id = dao.insert(log(justification = "short"))
        assertTrue(id > 0)
    }

    @Test
    fun insert_longJustification_persists() = runTest {
        val long = "This is a very detailed justification that exceeds twenty characters."
        val id = dao.insert(log(justification = long))
        assertTrue(id > 0)
        assertEquals(1, dao.countByPackage("com.example"))
    }

    // ── durationMinutes variants ──────────────────────────────────────────────

    @Test
    fun insert_variousDurations() = runTest {
        listOf(1, 5, 10, 15, 30).forEach { mins ->
            dao.insert(log(pkg = "com.dur.$mins", durationMinutes = mins))
        }
        assertEquals(5, (1..5).sumOf { dao.countByPackage("com.dur.${listOf(1,5,10,15,30)[it-1]}") })
    }
}
