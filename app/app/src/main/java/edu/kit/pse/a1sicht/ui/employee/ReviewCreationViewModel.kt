package edu.kit.pse.a1sicht.ui.employee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.database.entities.Timeslot
import edu.kit.pse.a1sicht.networking.EmployeeService
import edu.kit.pse.a1sicht.networking.RetrofitClient
import edu.kit.pse.a1sicht.networking.ReviewService
import edu.kit.pse.a1sicht.networking.StudentService
import edu.kit.pse.a1sicht.repository.EmployeeRepository
import edu.kit.pse.a1sicht.repository.ReviewRepository
import edu.kit.pse.a1sicht.repository.TimeslotRepository
import java.sql.Timestamp

/**
 * This is a view model class that extends [AndroidViewModel] class and
 * is responsible for the reviews' and time slots' creation as well and
 * update, manages data for the [ReviewCreationActivity].
 *
 * @param application This is an application parameter
 *
 * @author Tihomir Georgiev
 *
 * @see [ReviewRepository]
 * @see [EmployeeRepository]
 * @see [TimeslotRepository]
 * @see [ReviewCreationActivity]
 */
@Suppress("DEPRECATION")
class ReviewCreationViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * This is a variable of the [ReviewRepository] class.
     */
    private val reviewRepository = ReviewRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(ReviewService::class.java),
        RetrofitClient.getInstance()!!.create(StudentService::class.java)
    )

    /**
     * This is a variable of the [EmployeeRepository] class.
     */
    private val employeeRepository = EmployeeRepository(
        LocalDatabase.getInstance(application), RetrofitClient.getInstance()!!.create(EmployeeService::class.java)
    )

    /**
     * This is a variable of the [TimeslotRepository] class.
     */
    private val timeSlotRepository = TimeslotRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(ReviewService::class.java),
        RetrofitClient.getInstance()!!.create(StudentService::class.java)
    )

    /**
     * Local variables
     */
    private var _employee: LiveData<Employee> = employeeRepository.getEmployee()
    private val _review: MutableLiveData<Review> = MutableLiveData()

    /**
     * This method returns the current logged in employee user.
     *
     * @return [Employee] Returns a live data object containing an employee
     */
    fun getEmployee(): LiveData<Employee> {
        return _employee
    }

    /**
     * This method creates a new review with time slots.
     * @param name This is the name of the review
     * @param room This is the location of the review
     * @param year This is the year of the review
     * @param month This is the month of the review
     * @param day This is the day of the review
     * @param hour This is the hour of the start time of review
     * @param minute This is the minute of the start time of the review
     * @param timeSlotLength This is duration of a single time slot for the review
     * @param numberOfTimeSlots This is the number of time slots for the review
     * @param info This is the additional information about the review
     * @param maxStudents This is the maximum number of students that can register for a single time
     * slot review.
     * @param id This is the id of the employee that creates the review
     */
    fun createReview(name: String, room: String,
                     year: Int, month: Int, day: Int, hour: Int, minute: Int, timeSlotLength: Int,
                     numberOfTimeSlots: Int, info: String?, maxStudents: Int, id: Int) {
        var newInfo:String
        if(info == null){
            newInfo = ""
        }else{
            newInfo = info
        }
        _review.value = reviewRepository.createReview(name, room, year, month, day, hour, minute, timeSlotLength,
            numberOfTimeSlots, id, newInfo
        )

        //Creating time slots for the review
        var timeslot = Timeslot()
        timeslot.startTime!!.hours = hour
        timeslot.startTime!!.minutes = minute
        for (i in 1..numberOfTimeSlots) {
            if (timeslot.startTime!!.minutes > 59) {
                timeslot.startTime!!.minutes -= 60
                timeslot.startTime!!.hours++

                timeslot = createTimeSlot(_review.value?.id!!, year, month, day, timeslot.startTime!!.hours,
                    timeslot.startTime!!.minutes, timeSlotLength, maxStudents
                )
            } else {
                timeslot = createTimeSlot(_review.value?.id!!, year, month, day, timeslot.startTime!!.hours,
                    timeslot.startTime!!.minutes, timeSlotLength, maxStudents
                )
            }
            timeslot.startTime!!.minutes += timeSlotLength
        }
    }

    /**
     * This method creates a new time slot.
     *
     * @param reviewId This is the review id for which the time slot belongs
     * @param year This is the year of the date of the time slot
     * @param month This is the month of the date of the time slot
     * @param day This is the day of month of the date of the time slot
     * @param hour This is the hour of the start time of the time slot
     * @param minute This is the minutes of the start time of the time slot
     *
     * @return [Timeslot] Returns the new created time slot
     */
    private fun createTimeSlot(
        reviewId: Int, year: Int, month: Int, day: Int, hour: Int, minute: Int,
        duration: Int, maxStudents: Int
    ): Timeslot {
        return timeSlotRepository.createForReview(reviewId, year, month, day, hour, minute, duration, maxStudents)!!
    }

    /**
     * This method updates a review and the time slots of the review.
     * (The number of time slot cannot be changed.)
     *
     * @param reviewId This is the id of the review for update
     * @param newReview This is the new review that need to replace the old one
     */
    fun updateReview(reviewId: Int, newReview: Review, maxStudents: Int) {

        val oldReview = reviewRepository.getReviewById(reviewId)
        newReview.date!!.year-=1900

        val review = Review(reviewId,newReview.name,newReview.room,newReview.date,newReview.timeSlotLength,oldReview!!.numberOfTimeSlots,oldReview.employee_id,newReview.info)
        reviewRepository.updateReviewById(reviewId, review)

        var timeSlot = Timeslot()
        timeSlot.startTime!!.hours = review.date!!.hours
        timeSlot.startTime!!.minutes = review.date!!.minutes

        timeSlotRepository.getAllForReview(reviewId)!!.forEach {
            if (timeSlot.startTime!!.minutes > 59) {
                timeSlot.startTime!!.minutes -= 60
                timeSlot.startTime!!.hours++

                timeSlot = Timeslot(
                    it.id!!, Timestamp(newReview.date!!.year, newReview.date!!.month,
                        newReview.date!!.date, timeSlot.startTime!!.hours, timeSlot.startTime!!.minutes,0,0),
                    review.timeSlotLength, maxStudents,it.currentStudents,it.belongsTo
                )
            } else {
                timeSlot = Timeslot(
                    it.id!!, Timestamp(newReview.date!!.year, newReview.date!!.month,
                        newReview.date!!.date, timeSlot.startTime!!.hours, timeSlot.startTime!!.minutes,0,0),
                    review.timeSlotLength, maxStudents,it.currentStudents,it.belongsTo
                )
            }
            timeSlotRepository.updateById(reviewId, it.id!!,timeSlot)
            timeSlot.startTime!!.minutes += review.timeSlotLength
        }
    }
}