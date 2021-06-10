package db.models

/**
 * The StudentTimeslot class represents the many-to-many relationship
 * between students and timeslots in the database
 * @author Stanimir Bozhilov, Martin Zahariev
 */
data class StudentTimeslot(val studentId : Int, val timeslotId : Int)