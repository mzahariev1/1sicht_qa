package repository

import db.models.Review
import db.models.Student
import org.jetbrains.exposed.sql.transactions.transaction
import db.tables.Reviews
import db.tables.Timeslots
import db.tables.Students
import db.tables.StudentsTimeslots
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime

/**
 * The ReviewRepository class contains the methods which manipulate the data in the database table Review
 * @author Stanimir Bozhilov, Martin Zahariev
 */
class ReviewRepository {

    /**
     * This method creates a new tuple in the Reviews table in the database
     * @param review This object holds the attributes of the newly created tuple
     */
    fun create(review: Review): Review? {
        return transaction {
            val reviewId = Reviews.insert {
                it[name] = review.name
                it[location] = review.location
                it[date] = review.date
                it[lengthOfTimeslot] = review.lengthOfTimeslot
                it[numberOfTimeslots] = review.numberOfTimeslots
                it[createdBy] = review.createdBy
                it[description] = review.description
            } get Reviews.id

            return@transaction Reviews.select { Reviews.id eq reviewId }
                .map {
                    Review(
                        id = it[Reviews.id],
                        name = it[Reviews.name],
                        location = it[Reviews.location],
                        date = it[Reviews.date],
                        lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                        numberOfTimeslots = it[Reviews.numberOfTimeslots],
                        createdBy = it[Reviews.createdBy],
                        description = it[Reviews.description]
                    )
                }.firstOrNull()
        }
    }

    /**
     * This method gets all reviews from the database
     * @return A list with Review objects representing all tuples from the Employees table in the database
     */
    fun getAll(): List<Review> {
        return transaction {
            return@transaction Reviews.selectAll()
                .map {
                    Review(
                        id = it[Reviews.id],
                        name = it[Reviews.name],
                        location = it[Reviews.location],
                        date = it[Reviews.date],
                        lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                        numberOfTimeslots = it[Reviews.numberOfTimeslots],
                        createdBy = it[Reviews.createdBy],
                        description = it[Reviews.description]
                    )
                }

        }
    }

    /**
     * This methods fetches the review with the given ID number from the database
     * @param reviewId An Integer representing the unique identification number of the review
     * @return A Review object representing the tuple with the given ID number
     * or null if such a tuple does not exist.
     */
    fun getById(reviewId: Int): Review? {
        return transaction {
            return@transaction Reviews.select { Reviews.id eq reviewId }
                .map {
                    Review(
                        id = it[Reviews.id],
                        name = it[Reviews.name],
                        location = it[Reviews.location],
                        date = it[Reviews.date],
                        lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                        numberOfTimeslots = it[Reviews.numberOfTimeslots],
                        createdBy = it[Reviews.createdBy],
                        description = it[Reviews.description]
                    )
                }.firstOrNull()
        }
    }

    /**
     * This method searches for a review by the given keywords
     * @param keywords A String Array containing the keywords for the search
     * @return A List of Review objects which have matched
     */
    fun getByKeywords(keywords: Array<String>): List<Review> {
        var matchingReviews: MutableList<Review> = mutableListOf<Review>()
        return transaction {
            for (word in keywords) {
                val matchedReviewForOneWord = Reviews.select { Reviews.name.like("%$word%") or Reviews.description.like("%$word%") }
                    .map {
                        Review(
                            id = it[Reviews.id],
                            name = it[Reviews.name],
                            location = it[Reviews.location],
                            date = it[Reviews.date],
                            lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                            numberOfTimeslots = it[Reviews.numberOfTimeslots],
                            createdBy = it[Reviews.createdBy],
                            description = it[Reviews.description]
                        )
                    }

                if (matchedReviewForOneWord.isNotEmpty()) {
                    matchingReviews.addAll(matchedReviewForOneWord)
                }
            }

            return@transaction matchingReviews
        }
    }

    /**
     * This method updates the data for a review with the given ID number.
     * @param reviewId An Integer representing the unique identification number of the tuple to be updated
     * @param review An Review object holding the new attributes of the tuple
     */
    fun updateById(reviewId: Int, review: Review): Review? {
        return transaction {
            Reviews.update ({ Reviews.id eq reviewId }) {
                it[Reviews.name] = review.name
                it[Reviews.location] = review.location
                it[Reviews.date] = review.date
                it[Reviews.lengthOfTimeslot] = review.lengthOfTimeslot
                it[Reviews.numberOfTimeslots] = review.numberOfTimeslots
                it[Reviews.createdBy] = review.createdBy
                it[Reviews.description] = review.description
            }

            return@transaction Reviews.select { Reviews.id eq reviewId }
                .map {
                    Review(
                        id = it[Reviews.id],
                        name = it[Reviews.name],
                        location = it[Reviews.location],
                        date = it[Reviews.date],
                        lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                        numberOfTimeslots = it[Reviews.numberOfTimeslots],
                        createdBy = it[Reviews.createdBy],
                        description = it[Reviews.description]
                    )
                }.firstOrNull()
        }
    }

    /**
     * This method deletes a tuple with the given ID number from the Reviews table
     * @param reviewId An Integer representing the unique identification number of the tuple to be deleted
     */
    fun deleteById(reviewId: Int): Review? {
        return transaction {
            val deletedReview = Reviews.select { Reviews.id eq reviewId }
                .map {
                    Review(
                        id = it[Reviews.id],
                        name = it[Reviews.name],
                        location = it[Reviews.location],
                        date = it[Reviews.date],
                        lengthOfTimeslot = it[Reviews.lengthOfTimeslot],
                        numberOfTimeslots = it[Reviews.numberOfTimeslots],
                        createdBy = it[Reviews.createdBy],
                        description = it[Reviews.description]
                    )
                }.firstOrNull()

            Timeslots.deleteWhere { Timeslots.belongsTo eq reviewId }
            Reviews.deleteWhere { Reviews.id eq reviewId }

            return@transaction deletedReview
        }
    }

    /**
     * This method gets all students for a specific
     * @param reviewId An Integer representing the unique identification number of the review
     */
    fun getAllStudents(reviewId: Int): List<Student> {
        return transaction {
            return@transaction Reviews.join(Timeslots, JoinType.INNER, additionalConstraint = {Timeslots.belongsTo eq reviewId})
                .join(StudentsTimeslots, JoinType.INNER, additionalConstraint = {Timeslots.id eq StudentsTimeslots.timeslotId})
                .join(Students, JoinType.INNER, additionalConstraint = {StudentsTimeslots.studentId eq Students.id})
                .slice(Students.id, Students.googleId, Students.firstName, Students.lastName, Students.matriculationNumber)
                .select { Reviews.id eq reviewId }
                .map {
                    Student(
                        id = it[Students.id],
                        googleId = it[Students.googleId],
                        firstName = it[Students.firstName],
                        lastName = it[Students.lastName],
                        matriculationNumber = it[Students.matriculationNumber]
                    )
                }
        }
    }
}