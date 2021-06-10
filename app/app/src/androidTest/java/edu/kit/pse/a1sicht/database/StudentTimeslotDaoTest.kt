package edu.kit.pse.a1sicht.database

import  android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.kit.pse.a1sicht.LiveDataTestUtil
import edu.kit.pse.a1sicht.database.daos.*
import edu.kit.pse.a1sicht.database.entities.*
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.sql.Timestamp

/**
 * Test class for the [StudentTimeslotDao] class.
 *
 * Created on 10/7/2019
 * @author Tihomir Georgiev
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
open class StudentTimeslotDaoTest {
    /**
     * Data access object variable for [Student] entity
     */
    private lateinit var studentDao: StudentDao
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
     * Data access object variable for [StudentTimeslotDao] entity
     */
    private lateinit var std_ts_Dao: StudentTimeslotDao
    /**
     * Data base variable of the [LocalDatabase] class
     */
    private lateinit var db: LocalDatabase
    /**
     * [Employee] variable for tests
     */
    private var employee: Employee = Employee(
        1,
        "#####",
        "first name",
        "last name",
        true
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
    /**
     * [Student] variables for tests
     */
    private var student1 = Student(1, "######", "first name", "last name", 111111)
    private var student2 = Student(2, "######", "first name", "last name", 111111)
    private var student3 = Student(3, "######", "first name", "last name", 111111)

    /**
     * [StudentTimeslot] variables for tests
     */
    private var std_ts_1 = StudentTimeslot(1, 1)
    private var std_ts_2 = StudentTimeslot(2, 1)
    private var std_ts_3 = StudentTimeslot(3, 1)
    private var std_ts_4 = StudentTimeslot(3, 2)


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
        std_ts_Dao = db.studentTimeslotDao()
        studentDao = db.studentDao()
    }

    /**
     * Destroys the local database after the execution of the tests.
     */
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertTest() {
        userDao.insert(employee)
        studentDao.insert(student1)
        studentDao.insert(student2)
        studentDao.insert(student3)
        reviewDao.insert(review1)
        timeslotDao.insert(timeslot1)
        timeslotDao.insert(timeslot2)
        timeslotDao.insert(timeslot3)

        std_ts_Dao.insert(std_ts_1)
        std_ts_Dao.insert(std_ts_1)

        val getStudents = LiveDataTestUtil.getValue(std_ts_Dao.getAllStudentIdsForTimeSlot(1))
        Assert.assertEquals(getStudents.first(), 1)
        Assert.assertEquals(getStudents.size, 1)

        std_ts_Dao.insert(std_ts_2)
        std_ts_Dao.insert(std_ts_3)
        std_ts_Dao.insert(std_ts_4)
        val getStudents2 = LiveDataTestUtil.getValue(std_ts_Dao.getAllStudentIdsForTimeSlot(1))
        Assert.assertEquals(getStudents2.size, 3)

        val getTimeSlots = LiveDataTestUtil.getValue(std_ts_Dao.getAllTimeSlotIdsForStudent(3))
        Assert.assertEquals(getTimeSlots.size, 2)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTest() {
        userDao.insert(employee)
        studentDao.insert(student1)
        studentDao.insert(student2)
        studentDao.insert(student3)
        reviewDao.insert(review1)
        timeslotDao.insert(timeslot1)
        timeslotDao.insert(timeslot2)
        timeslotDao.insert(timeslot3)

        std_ts_Dao.insert(std_ts_1)
        std_ts_Dao.insert(std_ts_1)

        studentDao.deleteById(1)
        std_ts_Dao.insert(std_ts_2)
        std_ts_Dao.insert(std_ts_3)
        std_ts_Dao.insert(std_ts_4)

        val getStudents = LiveDataTestUtil.getValue(std_ts_Dao.getAllStudentIdsForTimeSlot(1))
        Assert.assertEquals(getStudents.size, 2)


        std_ts_Dao.delete(std_ts_3) //deleteById??
        val getStudents2 = LiveDataTestUtil.getValue(std_ts_Dao.getAllStudentIdsForTimeSlot(1))
        Assert.assertEquals(getStudents2.size, 1)

        val getTimeSlots = LiveDataTestUtil.getValue(std_ts_Dao.getAllTimeSlotIdsForStudent(3))
        Assert.assertEquals(getTimeSlots.size, 1)
    }
}