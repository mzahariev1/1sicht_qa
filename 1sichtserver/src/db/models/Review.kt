package db.models

import org.joda.time.DateTime

/**
 * The Review class represents the model of a review in the database
 * @author Stanimir Bozhilov, Martin Zahariev
 */
data class Review(val id: Int?, val name: String, val location: String, val date: DateTime,
                  val lengthOfTimeslot: Int, val numberOfTimeslots: Int,
                  val createdBy: Int, val description: String)