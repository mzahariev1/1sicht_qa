package database

import db.Database
import junit.framework.Assert.assertNotNull
import org.junit.Test

class DatabaseTest {

    @Test
    fun initializeDatabaseTest() {
        val db = Database.setupDB()

        assertNotNull(db)

    }



}