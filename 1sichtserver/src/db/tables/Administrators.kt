package db.tables

import org.jetbrains.exposed.sql.Table

/**
 * The Administrators object represents the Administrators table in the database.
 * @author Stanimir Bozhilov, Martin Zahariev
 */
object Administrators : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val googleId = varchar("google_id", 512)
    val firstName = varchar("first_name", 64)
    val lastName = varchar("last_name", 64)
}