package edu.kit.pse.a1sicht.ui.student

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.Student
import edu.kit.pse.a1sicht.database.entities.Timeslot
import edu.kit.pse.a1sicht.networking.RetrofitClient
import edu.kit.pse.a1sicht.networking.ReviewService
import edu.kit.pse.a1sicht.networking.StudentService
import edu.kit.pse.a1sicht.repository.StudentRepository
import edu.kit.pse.a1sicht.repository.TimeslotRepository

class StudentReviewViewModel(application: Application) : AndroidViewModel(Application()) {
    private val studentRepository = StudentRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(StudentService::class.java)
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
     * Variables for managing data for review and time slot
     */
    private val _student: LiveData<Student> = studentRepository.getStudent()
    private val _timeSlots: MutableLiveData<List<Timeslot>> = MutableLiveData()
    private val _timeSlotDate: MutableLiveData<String> = MutableLiveData()


    /**
     * This method gets the current logged in student user.
     * @return Live data object containing a student
     */
    fun getStudent(): LiveData<Student> {
        return _student
    }

    /**
     * This method gets the time slots for a review.
     * @param reviewId This is the id of the review
     * @return Live data object containing a list with time slots
     */
    fun getTimeSlotsForReview(reviewId: Int): LiveData<List<Timeslot>> {
        _timeSlots.value = timeSlotRepository.getAllForReview(reviewId)
        return _timeSlots
    }

    /**
     * This method gets the time slot's date for which student is signed for based on review.
     * @param reviewId This is the id of the review
     * @param studentId This is the id of the student
     * @return Live data object containing a date formatted to string
     */
    fun getTimeSlotDate(reviewId: Int, studentId: Int): LiveData<String> {
        timeSlotRepository.getAllForStudent(studentId)!!.forEach {
            if (it.belongsTo == reviewId) {
                _timeSlotDate.value = it.startTime.toString()
            }
        }
        return _timeSlotDate
    }

    /**
     * This method signs up student for time slot of a review.
     * @param studentId This is the id of the student
     * @param timeslotId This is the id of the time slot
     * @param reviewId This is the id of the review
     * @return False if the time slot is full, true else
     */
    @Synchronized
    fun signUpForTimeSlot(studentId: Int, timeslotId: Int, reviewId: Int): Boolean {
        val timeSlot = timeSlotRepository.getById(reviewId, timeslotId)
        return if (timeSlot?.currentStudents == timeSlot?.maxStudents) {
            false
        } else {
            timeSlotRepository.signUp(studentId, timeslotId)
            timeSlotRepository.updateById(
                reviewId, timeslotId,
                Timeslot(
                    timeslotId, timeSlot!!.startTime, timeSlot.duration, timeSlot.maxStudents,
                    timeSlot.currentStudents + 1, timeSlot.belongsTo
                )
            )
            true
        }
    }

    /**
     * This method signs out student from a review.
     * @param studentId This is the id of the student
     * @param reviewId This is the id of the review
     * @return False if the review signed up students equals zero, true else
     */
    @Synchronized
    fun signOutFromTimeSlot(studentId: Int, reviewId: Int): Boolean {
        var timeSlot = Timeslot()
        timeSlotRepository.getAllForStudent(studentId)!!.forEach {
            if(it.belongsTo == reviewId){
                timeSlot = it
            }
        }
        return if (timeSlot.currentStudents == 0) {
            false
        } else {
            timeSlotRepository.signOut(studentId, timeSlot.id!!)
            timeSlotRepository.updateById(
                reviewId, timeSlot.id!!,
                Timeslot(
                    timeSlot.id!!, timeSlot.startTime, timeSlot.duration, timeSlot.maxStudents,
                    timeSlot.currentStudents -1, timeSlot.belongsTo
                )
            )
            true
        }
    }

}