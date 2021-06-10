package edu.kit.pse.a1sicht.repository

import edu.kit.pse.a1sicht.networking.ReviewService
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.StudentTimeslot
import edu.kit.pse.a1sicht.database.entities.Timeslot
import edu.kit.pse.a1sicht.networking.StudentService
import edu.kit.pse.a1sicht.repository.tasks.StudentTimeSlotAsyncTask
import edu.kit.pse.a1sicht.repository.tasks.TimeSlotAsyncTask
import java.sql.Timestamp

/**
 * The [TimeslotRepository] class gives read and write access to Timeslot objects to the local and server localDatabase
 *
 * @author Maximilian Ackermann, Stanimir Bozhilov, Martin Zahariev
 */
@Suppress("DEPRECATION")
class TimeslotRepository constructor(localDatabase : LocalDatabase, private val reviewService: ReviewService, private val studentService: StudentService){

    private val timeslotDao = localDatabase.timeslotDao()
    private val studentTimeslotDao = localDatabase.studentTimeslotDao()

    private val timeSlotAsyncTask: TimeSlotAsyncTask = TimeSlotAsyncTask()
    private val studentTimeSlotAsyncTask: StudentTimeSlotAsyncTask = StudentTimeSlotAsyncTask()
    /**
     * Gets all timeslots for a review from the server and inserts them into the local localDatabase
     * @param [reviewId] The unique ID number of the review.
     * @return [List<Timeslot>] containing all timeslots for the given review.
     */
    fun getAllForReview(reviewId: Int): List<Timeslot>? {
        var timeslots:List<Timeslot> = ArrayList()
        try {
            timeslots = reviewService.getAllTimeslots(reviewId).blockingFirst()
        }catch (e:Exception){

        }
        timeSlotAsyncTask.deleteTimeslot(timeslotDao)
        timeslots.forEach {
            timeSlotAsyncTask.insertTimeslot(timeslotDao,it)
        }

        return timeslots
    }

    /**
     * Gets all timeslots for which a given student has signed up from the server and inserts them into the local localDatabase
     * @param [studentId] The unique ID number of the student.
     * @return [List<Timeslot>] containing all timeslots for the given student.
     */
    fun getAllForStudent(studentId: Int): List<Timeslot>? {
        var timeslots:List<Timeslot> = ArrayList()
        try {
            timeslots = studentService.getTimeslots(studentId).blockingFirst()
        }catch (e:Exception){

        }
        timeSlotAsyncTask.deleteTimeslot(timeslotDao)
        timeslots.forEach {
            timeSlotAsyncTask.insertTimeslot(timeslotDao,it)
        }
        return timeslots
    }


    /**
     * This method gets the timeslot for which a student has signed up in a given review
     * and inserts it into the local database
     * @param [studentId]
     * @param [reviewId]
     * @return The timeslot for which the student has signed up
     * or [null] if the student has not signed up for any of the timeslots in the review
     */
    fun getForStudentInReview(studentId: Int, reviewId: Int): Timeslot?{
        val timeslot = studentService.getTimeslotInReview(studentId, reviewId).blockingFirst()
        timeSlotAsyncTask.insertTimeslot(timeslotDao,timeslot)

        return timeslot
    }

    /**
     * This method creates a new Timeslot object with the given data and inserts it in both local and remote databases
     * @param [reviewId] The unique ID number of the review
     * @param [year] An Integer representing the year of the timeslot
     * @param [month] An Integer representing the month of the timeslot
     * @param [day] An Integer representing the day of the timeslot
     * @param [hour] An Integer representing the hour of the timeslot
     * @param [minute] An Integer representing the minutes of the timeslot
     * @param [duration] An Integer representing the duration of the timeslot
     * @param [maxStudents] An Integer representing the maximum number of students which are allowed to sign up for this timeslot
     * @return [Timeslot] object representing the newly created timeslot
     */
    fun createForReview(reviewId: Int, year: Int, month: Int, day: Int, hour: Int, minute: Int,
                                duration: Int, maxStudents: Int): Timeslot? {

        var timeslot = Timeslot(reviewId, Timestamp(year-1900, month, day, hour, minute, 0, 0),
            duration, maxStudents, 0, reviewId)


        timeslot = reviewService.createTimeslot(reviewId, timeslot).blockingFirst()
        timeSlotAsyncTask.insertTimeslot(timeslotDao,timeslot)

        return timeslot
    }

    /**
     * Gets a timeslot from the server and inserts it into the local localDatabase
     * @param [reviewId] The ID number of the review to which the timeslot belongs
     * @param [timeslotId] The ID number of the timeslot
     * @return [Timeslot] with the given ID number
     */
    fun getById(reviewId: Int ,timeslotId: Int): Timeslot? {
        val timeSlot = reviewService.getTimeslotById(reviewId, timeslotId).blockingFirst()
        timeSlotAsyncTask.insertTimeslot(timeslotDao,timeSlot)

        return timeSlot
    }

    /**
     * This method updates a timeslot with the given ID number and inserts it in both local and remote databases
     * @param [reviewId] The ID number of the review
     * @param [timeslotId] The ID number of the timeslot
     * @param [timeslot] A Timeslot object holding the new data
     * @return [Timeslot] The updated timeslot
     */
    fun updateById(reviewId: Int, timeslotId: Int, timeslot: Timeslot): Timeslot? {
        val timeSlot = reviewService.updateTimeslotById(reviewId, timeslotId, timeslot).blockingFirst()
        timeSlotAsyncTask.insertTimeslot(timeslotDao,timeSlot)

        return timeSlot
    }

    /**
     * Deletes a timeslot with the given ID number from both local and remote databases
     * @param [reviewId] The ID number of the review
     * @param [timeslotId] The ID number of the timeslot
     * @return [Timeslot] The deleted timeslot object
     */
    fun deleteById(reviewId: Int, timeslotId: Int): Timeslot? {
        timeSlotAsyncTask.deleteTimeSlotById(timeslotDao,timeslotId)
        return reviewService.deleteTimeslotById(reviewId, timeslotId).blockingFirst()
    }

    /**
     * This method signs up a student for a timeslot with the given ID number
     * @param [studentId] The ID number of the student
     * @param [timeslotId] The ID number of the timeslot
     * @return [true] if the student has signed up successfully, [false] otherwise
     */
    fun signUp(studentId: Int, timeslotId: Int) {
        studentTimeSlotAsyncTask.insertStudentTimeslot(studentTimeslotDao, StudentTimeslot(studentId,timeslotId))
        reviewService.signUpForTimeslot(timeslotId, studentId).execute()
    }

    /**
     * This method signs out a student from a timeslot with the given ID number
     * @param [studentId] The ID number of the student
     * @param [timeslotId] The ID number of the timeslot
     * @return [true] if the student has signed out successfully, false otherwise
     */
    fun signOut(studentId: Int, timeslotId: Int) {
        studentTimeSlotAsyncTask.deleteStudentTimeslot(studentTimeslotDao,StudentTimeslot(studentId,timeslotId))
         reviewService.signOutFromTimeslot(timeslotId, studentId).execute()
    }

    /**
     * This method changes the timeslot for which a student has signed up.
     * @param [oldTimeslotId] The ID number of the old timeslot
     * @param [newTimeslotId] The ID number of the new timeslot
     * @param [studentId] The ID number of the student
     * @return [Timeslot] The new timeslot for which the student has signed up.
     */
    fun changeTimeslotOfStudent(oldTimeslotId: Int, newTimeslotId: Int, studentId: Int): Timeslot {
        studentTimeSlotAsyncTask.deleteStudentTimeslot(studentTimeslotDao,StudentTimeslot(studentId,oldTimeslotId))
        studentTimeSlotAsyncTask.insertStudentTimeslot(studentTimeslotDao, StudentTimeslot(studentId,newTimeslotId))

        return reviewService.changeTimeslotOfStudent(oldTimeslotId, newTimeslotId, studentId).blockingFirst()
    }
}