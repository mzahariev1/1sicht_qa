package db.tables

import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.Table


/**
 * The @see<Reviews> object represents the Reviews table in the database.
 * @author Stanimir Bozhilov, Martin Zahariev
 */
object Reviews : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 64)
    val location = varchar("location", 64)
    val date = datetime("date")
    val lengthOfTimeslot = integer("length_of_timeslot")
    val numberOfTimeslots = integer("number_of_timeslots")
    val createdBy = integer("created_by").references(Employees.id, onDelete = CASCADE)
    val description = text("description")
}