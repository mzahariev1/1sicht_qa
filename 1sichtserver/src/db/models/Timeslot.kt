package db.models

import org.joda.time.DateTime

/**
 * The Timeslot class represents the model of a timeslot in the database
 * @author Stanimir Bozhilov, Martin Zahariev
 */
data class Timeslot(val id: Int?, val startTime: DateTime, val duration: Int,
                    val maxNumberOfStudents: Int, val currentNumberOfStudents: Int, val belongsTo: Int)