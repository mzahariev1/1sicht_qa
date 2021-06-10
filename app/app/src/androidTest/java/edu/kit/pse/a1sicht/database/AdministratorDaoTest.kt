package edu.kit.pse.a1sicht.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import edu.kit.pse.a1sicht.LiveDataTestUtil
import edu.kit.pse.a1sicht.database.daos.AdministratorDao
import edu.kit.pse.a1sicht.database.entities.Administrator
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This class tests the methods implemented in the [AdministratorDao]
 * class for the [Administrator] entity.
 *
 * Created on 10/7/2019
 * @author Tihomir Georgiev
 * @see AdministratorDao
 * @see Administrator
 */
@Suppress("DEPRECATION")
@RunWith(AndroidJUnit4::class)
open class AdministratorDaoTest {

    /**
     * Data access object variable for the [Administrator] entity
     */
    private lateinit var adminDao: AdministratorDao
    /**
     * [LocalDatabase] variable for testing
     */
    private lateinit var db: LocalDatabase
    /**
     * [Administrator] variable for testing
     */
    private var administrator: Administrator = Administrator(
        1,
        "#####",
        "first name",
        "last name"
    )

    @get:Rule
    val rule = InstantTaskExecutorRule()

    /**
     * Creates a new local database for the tests
     */
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        //Creates data base
        db = Room.inMemoryDatabaseBuilder(
            context, LocalDatabase::class.java
        ).build()
        //Gets the data access object for Administrator from the data base
        adminDao = db.adminDao()
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
     * Tests the [AdministratorDao.insert] and [AdministratorDao.get] methods
     * @throws Exception On failed test
     */
    @Test
    @Throws(Exception::class)
    fun insertAdministratorTest() {
        //Insert the administrator administrator
        adminDao.insert(administrator)

        val getUser = LiveDataTestUtil.getValue(adminDao.get())
        //Check the if the administrator equals to the administrator
        Assert.assertEquals(getUser, administrator)
    }

    /**
     * Tests the [AdministratorDao.update],[AdministratorDao.insert] and
     * [AdministratorDao.get] methods
     * @throws Exception On failed test
     */
    @Test
    @Throws(Exception::class)
    fun updateAdministratorTest() {
        //Insert the administrator
        adminDao.insert(administrator)
        val oldUser = Administrator(
            1, "#####", "first name",
            "last name"
        )
        //Change the last name of the administrator
        administrator.lastName = "new_last_name"

        //Update the administrator in the data base
        adminDao.update(administrator)
        val getUser = LiveDataTestUtil.getValue(adminDao.get())

        //Check if the administrator is updated
        Assert.assertEquals(getUser.lastName, "new_last_name")
        Assert.assertEquals(getUser.googleID, "#####")
        Assert.assertNotEquals(getUser, oldUser)
    }

    /**
     * Tests the [AdministratorDao.deleteById],[AdministratorDao.insert] and
     *[AdministratorDao.get] methods
     * @throws Exception On failed test
     */
    @Test
    @Throws(Exception::class)
    fun deleteAdministratorTest() {
        //Insert the administrator
        adminDao.insert(administrator)
        //Delete the administrator
        adminDao.deleteById(administrator.id!!)

        val getUser = LiveDataTestUtil.getValue(adminDao.get())
        Assert.assertEquals(getUser, null)
    }
}