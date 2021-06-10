package repository

import db.models.StudentTimeslot
import db.models.Timeslot
import db.tables.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Time

/**
 * The TimeslotRepository class contains the methods which manipulate the data in the database table Timeslots
 * @author Stanimir Bozhilov, Martin Zahariev
 */
class TimeslotRepository {

    /**
     * This method gets all timeslots which belong to a review with a given identification number
     * @param reviewId
     * @return A List of Timeslot objects containing all timeslots for the review with the given ID number
     */
    fun getAllForReview(reviewId: Int): List<Timeslot> {
        return transaction {
            return@transaction Timeslots.select { Timeslots.belongsTo eq reviewId }
                .map {
                    Timeslot(
                        id = it[Timeslots.id],
                        startTime = it[Timeslots.startTime],
                        duration = it[Timeslots.duration],
                        maxNumberOfStudents = it[Timeslots.maxNumberOfStudents],
                        currentNumberOfStudents = it[Timeslots.currentNumberOfStudents],
                        belongsTo = it[Timeslots.belongsTo]
                    )
                }
        }
    }

    /**
     * This method creates a new timeslot for a review with a given ID number
     * @param reviewId An Integer representing the unique identification number of the tuple in the database
     * @param timeslot A Timeslot object holding the attributes of the newly created timeslot
     */
    fun createForReview(reviewId: Int, timeslot: Timeslot): Timeslot? {
        return transaction {
            val timeslotId = Timeslots.insert {
                it[startTime] = timeslot.startTime
                it[duration] = timeslot.duration
                it[maxNumberOfStudents] = timeslot.maxNumberOfStudents
                it[currentNumberOfStudents] = timeslot.currentNumberOfStudents
                it[belongsTo] = reviewId
            } get Timeslots.id

            return@transaction Timeslots.select { Timeslots.id eq timeslotId }
                .map {
                    Timeslot(
                        id = it[Timeslots.id],
                        startTime = it[Timeslots.startTime],
                        duration = it[Timeslots.duration],
                        maxNumberOfStudents = it[Timeslots.maxNumberOfStudents],
                        currentNumberOfStudents = it[Timeslots.currentNumberOfStudents],
                        belongsTo = it[Timeslots.belongsTo]
                    )
                }.firstOrNull()
        }
    }

    /**
     * This method gets a timeslot with a given ID number for a review with a given ID number
     * @param reviewId An Integer representing the unique identification number of the review in the database
     * @param timeslotId An Integer representing the unique identification number of the timeslot in the database
     * @return A Timeslot object with a given ID number for a review with a given ID number
     */
    fun getForReviewById(reviewId: Int, timeslotId: Int): Timeslot? {
        return transaction {
            return@transaction Timeslots.select { (Timeslots.id eq timeslotId) and (Timeslots.belongsTo eq reviewId) }
                .map {
                    Timeslot(
                        id = it[Timeslots.id],
                        startTime = it[Timeslots.startTime],
                        duration = it[Timeslots.duration],
                        maxNumberOfStudents = it[Timeslots.maxNumberOfStudents],
                        currentNumberOfStudents = it[Timeslots.currentNumberOfStudents],
                        belongsTo = it[Timeslots.belongsTo]
                    )
                }.firstOrNull()
        }
    }

    /**
     * This method updates the information for a timeslot with a given ID number for a review with a given ID number
     * @param reviewId An Integer representing the unique identification number of the review in the database
     * @param timeslotId An Integer representing the unique identification number of the timeslot in the database
     * @param timeslot A Timeslot object holding the new attributes of the timeslot
     */
    fun updateForReviewById(reviewId: Int, timeslotId: Int, timeslot: Timeslot): Timeslot? {
        return transaction {
            Timeslots.update({ (Timeslots.id eq timeslotId) and (Timeslots.belongsTo eq reviewId) }) {
                it[startTime] = timeslot.startTime
                it[duration] = timeslot.duration
                it[maxNumberOfStudents] = timeslot.maxNumberOfStudents
                it[currentNumberOfStudents] = timeslot.currentNumberOfStudents
                it[belongsTo] = timeslot.belongsTo
            }

            return@transaction Timeslots.select { Timeslots.id eq timeslotId }
                .map {
                    Timeslot(
                        id = it[Timeslots.id],
                        startTime = it[Timeslots.startTime],
                        duration = it[Timeslots.duration],
                        maxNumberOfStudents = it[Timeslots.maxNumberOfStudents],
                        currentNumberOfStudents = it[Timeslots.currentNumberOfStudents],
                        belongsTo = it[Timeslots.belongsTo]
                    )
                }.firstOrNull()
        }
    }

    /**
     * This method deletes a tuple with the given ID number for a review with a given ID number from the Timeslots table
     * @param reviewId An Integer representing the unique identification number of the review in the database
     * @param timeslotId An Integer representing the unique identification number of the timeslot in the database
     */
    fun deleteForReviewById(reviewId: Int, timeslotId: Int): Timeslot? {
        return transaction {
            val deletedTimeslot = Timeslots.select { (Timeslots.id eq timeslotId) and (Timeslots.belongsTo eq reviewId) }
                .map {
                    Timeslot(
                        id = it[Timeslots.id],
                        startTime = it[Timeslots.startTime],
                        duration = it[Timeslots.duration],
                        maxNumberOfStudents = it[Timeslots.maxNumberOfStudents],
                        currentNumberOfStudents = it[Timeslots.currentNumberOfStudents],
                        belongsTo = it[Timeslots.belongsTo]
                    )
                }.firstOrNull()

            StudentsTimeslots.deleteWhere { (StudentsTimeslots.timeslotId eq timeslotId) }
            Timeslots.deleteWhere { (Timeslots.id eq timeslotId) and (Timeslots.belongsTo eq reviewId) }

            return@transaction deletedTimeslot
        }
    }

    /**
     * This methods signs up a student with a given ID number for a timeslot with a given ID number
     * @param timeslotId An Integer representing the unique identification number of the timeslot in the database
     * @param studentId An Integer representing the unique identification number of the student in the database
     * @return true if the sign-up procedure was successful, false otherwise
     */
    fun signUp(timeslotId: Int, studentId: Int): Boolean {
        return transaction {
            val signUpExists = StudentsTimeslots.select { (StudentsTimeslots.studentId eq studentId) and (StudentsTimeslots.timeslotId eq timeslotId) }
                .map {
                    StudentTimeslot(
                        studentId = it[StudentsTimeslots.studentId],
                        timeslotId = it[StudentsTimeslots.timeslotId]
                    )
                }.firstOrNull()

            if (signUpExists != null) {
                return@transaction false
            } else {
                StudentsTimeslots.insert {
                    it[StudentsTimeslots.studentId] = studentId
                    it[StudentsTimeslots.timeslotId] = timeslotId
                }

                return@transaction true
            }
        }
    }

    /**
     * This method signs out a student with a given ID number from a timeslot with a given ID number
     * @param timeslotId An Integer representing the unique identification number of the timeslot in the database
     * @param studentId An Integer representing the unique identification number of the student in the database
     * @return true if the sign-out procedure was successful, false otherwise
     */
    fun signOut(timeslotId: Int, studentId: Int): Boolean {
        return transaction {
            val signUpExists = StudentsTimeslots.select { (StudentsTimeslots.studentId eq studentId) and (StudentsTimeslots.timeslotId eq timeslotId) }
                .map {
                    StudentTimeslot(
                        studentId = it[StudentsTimeslots.studentId],
                        timeslotId = it[StudentsTimeslots.timeslotId]
                    )
                }.firstOrNull()

            if (signUpExists == null) {
                return@transaction false
            } else {
                StudentsTimeslots.deleteWhere { (StudentsTimeslots.studentId eq studentId) and (StudentsTimeslots.timeslotId eq timeslotId) }

                return@transaction true
            }
        }
    }

    /**
     * This method changes the timeslot for which the student has registered
     * @param oldTimeslotId An Integer representing the unique identification number of the old timeslot in the database
     * @param newTimeslotId An Integer representing the unique identification number of the new timeslot in the database
     * @param studentId An Integer representing the unique identification number of the student in the database
     * @return true if the change procedure was successful, false otherwise
     */
    fun changeTimeslotOfStudent(oldTimeslotId: Int, newTimeslotId: Int, studentId: Int): Boolean {
        var signOutSuccessful = signOut(oldTimeslotId, studentId)
        var signUpSuccessful = signUp(newTimeslotId, studentId)

        return signOutSuccessful && signUpSuccessful
    }

    /**
     * This method returns all timeslots for a student
     * @param studentId An Integer representing the unique identification number of the student
     * @return The list of timeslots belonging to this student
     */
    fun getAllForStudent(studentId: Int): List<Timeslot>? {
        return transaction {
            return@transaction StudentsTimeslots.join(Timeslots, JoinType.INNER, additionalConstraint = {StudentsTimeslots.timeslotId eq Timeslots.id})
                .slice(Timeslots.id, Timeslots.startTime, Timeslots.duration, Timeslots.maxNumberOfStudents, Timeslots.currentNumberOfStudents, Timeslots.belongsTo)
                .select { StudentsTimeslots.studentId eq studentId }
                .map {
                    Timeslot(
                        id = it[Timeslots.id],
                        startTime = it[Timeslots.startTime],
                        duration = it[Timeslots.duration],
                        maxNumberOfStudents = it[Timeslots.maxNumberOfStudents],
                        currentNumberOfStudents = it[Timeslots.currentNumberOfStudents],
                        belongsTo = it[Timeslots.belongsTo]
                    )
                }
        }
    }
}