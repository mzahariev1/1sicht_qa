package db.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.Table

/**
 * The @see<StudentsTimeslots> object represents the StudentsTimeslots table in the database.
 * @author Stanimir Bozhilov, Martin Zahariev
 */
object StudentsTimeslots : Table() {
    val studentId = integer("student_id").references(Students.id, onDelete = CASCADE).primaryKey(0)
    val timeslotId = integer("timeslot_id").references(Timeslots.id, onDelete = CASCADE).primaryKey(1)
}