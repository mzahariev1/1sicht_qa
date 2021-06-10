package edu.kit.pse.a1sicht.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.kit.pse.a1sicht.LiveDataTestUtil
import edu.kit.pse.a1sicht.database.daos.EmployeeDao
import edu.kit.pse.a1sicht.database.daos.ReviewDao
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Review
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.sql.Timestamp

/**
 * Test class for the [ReviewDao] class.
 *
 * @author Tihomir Georgiev
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
open class ReviewDaoTest {
    /**
     * Data access object variable for [Review] entity
     */
    private lateinit var reviewDao: ReviewDao
    /**
     * Data access object variable for [Employee] entity
     */
    private lateinit var userDao: EmployeeDao
    /**
     * Data base variable of the [LocalDatabase] class
     */
    private lateinit var db: LocalDatabase
    /**
     * [Review] variables for tests
     */
    private var review1: Review = Review(
        1, "name1", "place1", Timestamp(2001, 1, 1, 11, 11, 11, 11),
        1, 1, 1, "info1"
    )
    private var review2: Review = Review(
        2, "name2", "place2", Timestamp(2002, 1, 1, 11, 11, 11, 11),
        2, 2, 1, "info2"
    )
    private var review3: Review = Review(
        3, "name3", "place3", Timestamp(2003, 1, 1, 11, 11, 11, 11),
        3, 3, 2, "info3"
    )
    /**
     * [Employee] variables for tests
     */
    private var user1: Employee = Employee(

        1,
        "#####",
        "first name",
        "last name",
        true
    )
    private var user2: Employee = Employee(
        2,
        "#####",
        "first name",
        "last name",
        true
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
     * Tests the [ReviewDao.insert] and [ReviewDao.getAllByIds] methods.
     * Inserts same variable multiple times.
     */
    @Test
    @Throws(Exception::class)
    fun insertTest1() {
        userDao.insert(user1)

        //Insert the same review multiple times
        reviewDao.insert(review1)
        reviewDao.insert(review1)
        reviewDao.insert(review1)

        val getReview = LiveDataTestUtil.getValue(reviewDao.getAllByIds(arrayOf(1)))
        Assert.assertEquals(getReview.size, 1)
    }

    /**
     * Tests the [ReviewDao.insert], [ReviewDao.getAllByIds] and [ReviewDao.getAllByEmployee] methods.
     * Inserts multiple reviews.
     */
    @Test
    @Throws(Exception::class)
    fun insertTest2() {
        userDao.insert(user1)
        userDao.insert(user2)

        //Insert the reviews
        reviewDao.insert(review1)
        reviewDao.insert(review2)
        reviewDao.insert(review3)

        var getReviews = LiveDataTestUtil.getValue(reviewDao.getAllByIds(arrayOf(1, 2)))
        Assert.assertEquals(getReviews.size, 2)

        getReviews = LiveDataTestUtil.getValue(reviewDao.getAllByIds(arrayOf(3)))
        Assert.assertEquals(getReviews.size, 1)

        getReviews = LiveDataTestUtil.getValue(reviewDao.getAllByEmployee(1))
        Assert.assertEquals(getReviews.size, 2)

        getReviews = LiveDataTestUtil.getValue(reviewDao.getAllByEmployee(2))
        Assert.assertEquals(getReviews.first().name,"name3")
    }

    /**
     * Tests the [ReviewDao.update],[ReviewDao.insert] and [ReviewDao.getAllByIds] methods.
     */
    @Test
    @Throws(Exception::class)
    fun updateTest(){
        userDao.insert(user1)
        reviewDao.insert(review1)

        //Change the name of review
        review1.name = "new_name"

        //Update the review
        reviewDao.update(review1)
        var getReviews = LiveDataTestUtil.getValue(reviewDao.getAllByIds(arrayOf(1)))
        Assert.assertEquals(getReviews.first().name, "new_name")
    }

    /**
     * Tests the [ReviewDao.deleteById],[ReviewDao.insert] and [ReviewDao.getAllByIds] methods.
     */
    @Test
    @Throws(Exception::class)
    fun deleteById(){
        userDao.insert(user1)
        reviewDao.insert(review1)
        reviewDao.insert(review2)

        var getReviews = LiveDataTestUtil.getValue(reviewDao.getAllByIds(arrayOf(1, 2)))
        Assert.assertEquals(getReviews.size, 2)

        //Delete review1
        reviewDao.deleteById(1)

        getReviews = LiveDataTestUtil.getValue(reviewDao.getAllByIds(arrayOf(1, 2)))
        Assert.assertEquals(getReviews.size, 1)
    }
}