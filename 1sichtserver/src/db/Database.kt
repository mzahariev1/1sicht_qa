package db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import db.tables.*

/**
 * The Database class represents a connection to the database on the server side of the application
 * @author Stanimir Bozhilov, Martin Zahariev
 */
class Database {
    companion object {

        /**
         * This function establishes the connection to the database.
         */
        fun setupDB(): Unit {
            Database.connect("jdbc:mysql://localhost:3306/1sicht-db?useSSL=false&&allowPublicKeyRetrieval=true", driver = "com.mysql.jdbc.Driver",
                user = "1sicht", password = "1Sicht$.")

            transaction {
                SchemaUtils.create(Administrators, Employees, Reviews, Students, StudentsTimeslots, Timeslots)
            }
        }
    }
}