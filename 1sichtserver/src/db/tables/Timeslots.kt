package db.tables

import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime

/**
 * The @see<Timeslots> object represents the Timeslots table in the database.
 * @author Stanimir Bozhilov, Martin Zahariev
 */
object Timeslots : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val startTime = datetime("start_time")
    val duration = integer("duration")
    val maxNumberOfStudents = integer("max_number_of_students")
    val currentNumberOfStudents = integer("current_number_of_students")
    val belongsTo = integer("belongs_to").references(Reviews.id, onDelete = CASCADE)
}