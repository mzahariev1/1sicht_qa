package edu.kit.pse.a1sicht.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.kit.pse.a1sicht.LiveDataTestUtil
import edu.kit.pse.a1sicht.database.daos.EmployeeDao
import edu.kit.pse.a1sicht.database.daos.ReviewDao
import edu.kit.pse.a1sicht.database.daos.TimeslotDao
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.database.entities.Timeslot
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.sql.Timestamp

/**
 * Test class for the [TimeslotDao] class.
 *
 * @author Tihomir Georgiev
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
open class TimeslotDaoTest {
    /**
     * Data access object variable for [Review] entity
     */
    private lateinit var reviewDao: ReviewDao
    /**
     * Data access object variable for [Employee] entity
     */
    private lateinit var userDao: EmployeeDao
    /**
     * Data access object variable for [Timeslot] entity
     */
    private lateinit var timeslotDao: TimeslotDao
    /**
     * Data base variable of the [LocalDatabase] class
     */
    private lateinit var db: LocalDatabase
    /**
     * [Employee] variable for tests
     */
    private var user: Employee = Employee(
        1, "#####", "first name", "last name", true
    )
    /**
     * [Review] variable for tests
     */
    private var review1: Review = Review(
        1, "name1", "place1", Timestamp(2001, 1, 1, 11, 0, 0, 0),
        1, 1, 1, "info1"
    )
    /**
     * [Timeslot] variables for tests
     */
    private var timeslot1: Timeslot = Timeslot(
        1, Timestamp(2001, 1, 1, 11, 0, 0, 0),
        30, 15, 0, 1
    )
    private var timeslot2: Timeslot = Timeslot(
        2, Timestamp(2001, 1, 1, 11, 30, 0, 0),
        30, 10, 0, 1
    )
    private var timeslot3: Timeslot = Timeslot(
        3, Timestamp(2001, 1, 1, 12, 0, 0, 0),
        30, 15, 0, 1
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
        reviewDao = db.reviewDao()
        userDao = db.employeeDao()
        timeslotDao = db.timeslotDao()
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
     * Tests the [TimeslotDao.insert], [TimeslotDao.insertAll],[TimeslotDao.getById] and
     * [TimeslotDao.getAllByReview] methods.
     */
    @Test
    @Throws(Exception::class)
    fun insertTest() {
        userDao.insert(user)
        reviewDao.insert(review1)

        timeslotDao.insert(timeslot1)

        val getTimeSlot = LiveDataTestUtil.getValue(timeslotDao.getById(1))
        Assert.assertEquals(getTimeSlot.belongsTo, review1.id)
        Assert.assertEquals(getTimeSlot.startTime, Timestamp(2001, 1, 1, 11, 0, 0, 0))

        //timeslotDao.insertAll(listOf(timeslot2,timeslot1,timeslot3)) TODO

        val getTimeSlot1 = LiveDataTestUtil.getValue(timeslotDao.getById(2))
        Assert.assertEquals(getTimeSlot1.maxStudents, 10)

        val getTimeSlots = LiveDataTestUtil.getValue(timeslotDao.getAllByReview(1))
        Assert.assertEquals(getTimeSlots.size, 3)
    }

    /**
     * Tests the [TimeslotDao.insert],[TimeslotDao.getById] and
     * [TimeslotDao.update] methods.
     */
    @Test
    @Throws(Exception::class)
    fun updateTest() {
        userDao.insert(user)
        reviewDao.insert(review1)

        timeslotDao.insert(timeslot1)

        val getTimeSlot = LiveDataTestUtil.getValue(timeslotDao.getById(1))
        Assert.assertEquals(getTimeSlot.currentStudents, 0)

        timeslot1.currentStudents = 10
        timeslotDao.update(timeslot1)

        val getTimeSlot1 = LiveDataTestUtil.getValue(timeslotDao.getById(1))
        Assert.assertEquals(getTimeSlot1.currentStudents, 10)
    }

    /**
     * Tests the [TimeslotDao.insert],[TimeslotDao.getById] and
     * [TimeslotDao.deleteById] methods.
     */
    @Test
    @Throws(Exception::class)
    fun deleteTest() {
        userDao.insert(user)
        reviewDao.insert(review1)
        //timeslotDao.insertAll(listOf(timeslot1,timeslot2)) TODO

        var getTimeSlots = LiveDataTestUtil.getValue(timeslotDao.getAllByReview(1))
        //Check the size of the returned list
        Assert.assertEquals(getTimeSlots.size, 2)

        timeslotDao.deleteById(1)
        getTimeSlots = LiveDataTestUtil.getValue(timeslotDao.getAllByReview(1))

        //Check the size of the returned list
        Assert.assertEquals(getTimeSlots.size , 1)
        //Compare the first element of the list with timeslot2
        Assert.assertEquals(getTimeSlots.first() , timeslot2)
    }
}