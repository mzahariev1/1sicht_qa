package repository

import db.models.Review
import db.models.Student
import db.models.Timeslot
import org.jetbrains.exposed.sql.transactions.transaction
import db.tables.StudentsTimeslots
import db.tables.Timeslots
import db.tables.Students
import org.jetbrains.exposed.sql.*
import db.tables.Reviews

/**
 * The StudentRepository class contains the methods which manipulate the data in the database table Student
 * @author Stanimir Bozhilov, Martin Zahariev
 */
class StudentRepository {

    /**
     * This method creates a new tuple in the Students table in the database
     * @param student This object holds the attributes of the newly created tuple
     */
    fun create(student: Student): Student? {
        return transaction {
            val studentId = Students.insert {
                it[googleId] = student.googleId
                it[firstName] = student.firstName
                it[lastName] = student.lastName
                it[matriculationNumber] = student.matriculationNumber
            } get Students.id

            return@transaction Students.select { Students.id eq studentId }
                .map {
                    Student(
                        id = it[Students.id],
                        googleId = it[Students.googleId],
                        firstName = it[Students.firstName],
                        lastName = it[Students.lastName],
                        matriculationNumber = it[Students.matriculationNumber]
                    )
                }.firstOrNull()
        }

    }

    /**
     * This method gets all students from the database
     * @return A list with Student objects representing all tuples from the Students table in the database
     */
    fun getAll(): List<Student> {
        return transaction {
            return@transaction Students.selectAll()
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

    /**
     * This methods fetches the student with the given ID number from the database
     * @param studentId An Integer representing the unique identification number of the student
     * @return A Student object representing the tuple with the given ID number
     * or null if such a tuple does not exist.
     */
    fun getById(studentId: Int): Student? {
        return transaction {
            return@transaction Students.select { Students.id eq studentId }
                .map {
                    Student(
                        id = it[Students.id],
                        googleId = it[Students.googleId],
                        firstName = it[Students.firstName],
                        lastName = it[Students.lastName],
                        matriculationNumber = it[Students.matriculationNumber]
                    )
                }.firstOrNull()
        }
    }

    /**
     * This method updates the data for a student with the given ID number.
     * @param studentId An Integer representing the unique identification number of the tuple to be updated
     * @param student A Student object holding the new attributes of the tuple
     */
    fun updateById(studentId: Int, student: Student): Student? {
        return transaction {
            Students.update({ Students.id eq studentId }) {
                it[Students.googleId] = student.googleId
                it[Students.firstName] = student.firstName
                it[Students.lastName] = student.lastName
                it[Students.matriculationNumber] = student.matriculationNumber
            }

            return@transaction Students.select { Students.id eq studentId }
                .map {
                    Student(
                        id = it[Students.id],
                        googleId = it[Students.googleId],
                        firstName = it[Students.firstName],
                        lastName = it[Students.lastName],
                        matriculationNumber = it[Students.matriculationNumber]
                    )
                }.firstOrNull()

        }
    }

    /**
     * This method deletes a tuple with the given ID number from the Students table
     * @param studentId An Integer representing the unique identification number of the tuple to be deleted
     */
    fun deleteById(studentId: Int): Student? {
        return transaction {
            val deletedStudent = Students.select { Students.id eq studentId }
                .map {
                    Student(
                        id = it[Students.id],
                        googleId = it[Students.googleId],
                        firstName = it[Students.firstName],
                        lastName = it[Students.lastName],
                        matriculationNumber = it[Students.matriculationNumber]
                    )
                }.firstOrNull()

            StudentsTimeslots.deleteWhere { StudentsTimeslots.studentId eq studentId }
            Students.deleteWhere { Students.id eq studentId }

            return@transaction deletedStudent
        }
    }

    /**
     * This method gets all reviews for which a given student has registered
     * @param studentId An Integer representing the unique identification number of the student
     * @return A List of Review objects for which the student has registered
     */
    fun getReviewsForStudent(studentId: Int): List<Review> {
        return transaction {
            return@transaction ((StudentsTimeslots innerJoin Timeslots) innerJoin Reviews)
                .slice(
                    Reviews.id, Reviews.name, Reviews.location, Reviews.date, Reviews.lengthOfTimeslot,
                    Reviews.numberOfTimeslots, Reviews.createdBy, Reviews.description
                ).select { (StudentsTimeslots.studentId eq studentId) and (StudentsTimeslots.timeslotId eq Timeslots.id) and (Timeslots.belongsTo eq Reviews.id) }
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
     * This method returns all timeslots for a student
     * @param studentId An Integer representing the unique identification number of the student
     * @return The list of timeslots belonging to this student
     */
    fun getTimeslotsForStudent(studentId: Int): List<Timeslot> {
        return transaction {
            return@transaction StudentsTimeslots.join(
                Timeslots,
                JoinType.INNER,
                additionalConstraint = { StudentsTimeslots.timeslotId eq Timeslots.id })
                .slice(
                    Timeslots.id,
                    Timeslots.startTime,
                    Timeslots.duration,
                    Timeslots.maxNumberOfStudents,
                    Timeslots.currentNumberOfStudents,
                    Timeslots.belongsTo
                )
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

    /**
     * This method gets all students which have signed up for a given review
     * @param [reviewId] The unique ID number of the review
     * @return [List<Student>] containing all students which have signed up for the given review
     */
    fun getStudentsForReview(reviewId: Int): List<Student>? {
        return transaction {
            return@transaction Students.join(
                StudentsTimeslots,
                JoinType.INNER,
                additionalConstraint = { Students.id eq StudentsTimeslots.studentId })
                .slice(
                    Students.id,
                    Students.googleId,
                    Students.firstName,
                    Students.lastName,
                    Students.matriculationNumber
                )
                .select { (StudentsTimeslots.timeslotId eq Timeslots.id) and (Timeslots.belongsTo eq reviewId) }
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

    /**
     * This method gets all students which have signed up for a given timeslot
     * @param [timeslotId] The unique ID number of the timeslot
     * @return [List<Student>] containing all students which have signed up for the given timeslot
     */
    fun getStudentsForTimeslot(timeslotId: Int): List<Student>? {
        return transaction {
            return@transaction Students.join(
                StudentsTimeslots,
                JoinType.INNER,
                additionalConstraint = { Students.id eq StudentsTimeslots.studentId })
                .slice(
                    Students.id,
                    Students.googleId,
                    Students.firstName,
                    Students.lastName,
                    Students.matriculationNumber
                )
                .select { StudentsTimeslots.timeslotId eq timeslotId }
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

    /**
     * This method gets the timeslot for which a student has signed up in a given review
     * @param [studentId] The ID number of the student
     * @param [reviewId] The ID number of the review
     * @return [Timeslot] for which the student has signed up in the review
     */
    fun getTimeslotInReview(studentId: Int, reviewId: Int): Timeslot? {
        return transaction {
            return@transaction StudentsTimeslots.join(
                Timeslots,
                JoinType.INNER,
                additionalConstraint = { StudentsTimeslots.timeslotId eq Timeslots.id })
                .join(Reviews, JoinType.INNER, additionalConstraint = { Timeslots.belongsTo eq Reviews.id })
                .slice(
                    Timeslots.id,
                    Timeslots.startTime,
                    Timeslots.duration,
                    Timeslots.maxNumberOfStudents,
                    Timeslots.currentNumberOfStudents,
                    Timeslots.belongsTo
                )
                .select { (Students.id eq studentId) and (Reviews.id eq reviewId) }
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
}