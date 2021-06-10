package edu.kit.pse.a1sicht.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import edu.kit.pse.a1sicht.LiveDataTestUtil
import edu.kit.pse.a1sicht.database.daos.StudentDao
import edu.kit.pse.a1sicht.database.entities.Student
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Test class for the [StudentDao] class.
 *
 * @author Tihomir Georgiev
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
open class StudentDaoTest {
    private lateinit var userDao: StudentDao
    private lateinit var db: LocalDatabase
    private var user: Student = Student(
        1,
        "googleId",
        "first name",
        "last name",
        111111
    )

    @get:Rule
    val rule = InstantTaskExecutorRule()

    /**
     * Creates a new local database for the tests.
     */
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, LocalDatabase::class.java
        ).build()
        userDao = db.studentDao()
    }

    /**
     * Destroys the local database after the execution of the tests.
     */
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Tests the [StudentDao.insert] and [StudentDao.get] methods.
     */
    @Test
    @Throws(Exception::class)
    fun insertTest() {
        userDao.insert(user)

        var getUser = LiveDataTestUtil.getValue(userDao.get())
        assertEquals(getUser, user)
    }

    /**
     * Tests the [StudentDao.update],[StudentDao.insert] and [StudentDao.get] methods.
     */
    @Test
    @Throws(Exception::class)
    fun updateTest() {
        userDao.insert(user)
        user.lastName = "new_last_name"

        userDao.update(user)
        var getUser = LiveDataTestUtil.getValue(userDao.get())

        val oldUser = Student(
            1,
            "#####",
            "first name",
            "last name",
            111111
        )
        assertEquals(getUser.lastName, "new_last_name")
        assertEquals(getUser.googleID, "#####")
        assertNotEquals(getUser, oldUser)
    }

    /**
     * Tests the [StudentDao.deleteById],[StudentDao.insert] and [StudentDao.get] methods.
     */
    @Test
    @Throws(Exception::class)
    fun deleteByIdTest() {
        userDao.insert(user)
        userDao.deleteById(user.id!!)

        var getUser = LiveDataTestUtil.getValue(userDao.get())
        assertEquals(getUser, null)
    }
}