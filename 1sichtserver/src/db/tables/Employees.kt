package db.tables

import org.jetbrains.exposed.sql.Table

/**
 * The @see<Employees> object represents the Employees table in the database.
 * @author Stanimir Bozhilov, Martin Zahariev
 */
object Employees : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val googleId = varchar("google_id", 512)
    val firstName = varchar("first_name", 64)
    val lastName = varchar("last_name", 64)
    val verified = bool("verified").default(false)
}