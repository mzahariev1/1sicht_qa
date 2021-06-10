package edu.kit.pse.a1sicht.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import edu.kit.pse.a1sicht.LiveDataTestUtil
import edu.kit.pse.a1sicht.database.daos.EmployeeDao
import edu.kit.pse.a1sicht.database.entities.Employee
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Test class for the [EmployeeDao] class.
 *
 * Created on 10/7/2019
 * @author Tihomir Georgiev
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
open class EmployeeDaoTest {

    /**
     *
     */
    private lateinit var userDao: EmployeeDao
    private lateinit var db: LocalDatabase
    private var employee: Employee = Employee(
        1, "#####" , "first name",
        "last name",true
    )

    @get:Rule
    val rule = InstantTaskExecutorRule()

    /**
     * Creates a new local database for the tests
     */
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, LocalDatabase::class.java
        ).build()
        userDao = db.employeeDao()
    }

    /**
     * Destroys the local database after the execution of the tests
     * @throws IOException On error
     */
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Tests the [EmployeeDao.insert] and [EmployeeDao.get] methods
     * @throws Exception On failed test
     */
    @Test
    @Throws(Exception::class)
    fun insertTest() {
        userDao.insert(employee)

        var getUser = LiveDataTestUtil.getValue(userDao.get())
        Assert.assertEquals(getUser, employee)
    }

    /**
     * Tests the [EmployeeDao.update],[EmployeeDao.insert] and [EmployeeDao.get] methods
     * @throws Exception On failed test
     */
    @Test
    @Throws(Exception::class)
    fun updateTest() {
        userDao.insert(employee)
        employee.lastName = "new_last_name"

        userDao.update(employee)
        var getUser = LiveDataTestUtil.getValue(userDao.get())

        val oldUser = Employee(
            1, "####" , "first name",
            "last name",true
        )
        Assert.assertEquals(getUser.lastName, "new_last_name")
        Assert.assertEquals(getUser.googleID, "#####")
        Assert.assertNotEquals(getUser, oldUser)
    }

    /**
     * Tests the [EmployeeDao.deleteById],[EmployeeDao.insert] and [EmployeeDao.get] methods
     * @throws Exception On failed test
     */
    @Test
    @Throws(Exception::class)
    fun deleteByIdTest() {
        userDao.insert(employee)
        userDao.deleteById(employee.id!!)

        var getUser = LiveDataTestUtil.getValue(userDao.get())
        Assert.assertEquals(getUser, null)
    }
}