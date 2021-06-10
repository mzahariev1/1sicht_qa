package edu.kit.pse.a1sicht.ui.employee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.networking.RetrofitClient
import edu.kit.pse.a1sicht.networking.ReviewService
import edu.kit.pse.a1sicht.networking.StudentService
import edu.kit.pse.a1sicht.repository.EmployeeRepository
import edu.kit.pse.a1sicht.repository.ReviewRepository
import edu.kit.pse.a1sicht.repository.TimeslotRepository
import java.sql.Timestamp
import edu.kit.pse.a1sicht.ui.student.StudentReviewScreenActivity

/**
 * This is a view model class that extends [AndroidViewModel] class and
 * is responsible for managing review and time slot
 * data for [ReviewCreationActivity],[StudentReviewScreenActivity] and [ReviewScreenActivity].
 *
 * @param application This is an application parameter
 *
 * @author Tihomir Georgiev
 *
 * @see [ReviewRepository]
 * @see [EmployeeRepository]
 * @see [TimeslotRepository]
 * @see [ReviewCreationActivity]
 * @see [ReviewScreenActivity]
 * @see [StudentReviewScreenActivity]
 */
class ReviewScreenViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * This is a variable of the [ReviewRepository] class.
     */
    private val reviewRepository = ReviewRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(ReviewService::class.java),
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
    private val _review: MutableLiveData<Review> = MutableLiveData()
    private val _reviewName: MutableLiveData<String> = MutableLiveData()
    private val _reviewInfo: MutableLiveData<String> = MutableLiveData()
    private val _signedStudents: MutableLiveData<String> = MutableLiveData()
    private val _reviewLocation: MutableLiveData<String> = MutableLiveData()
    private val _reviewDateTime: MutableLiveData<Timestamp> = MutableLiveData()
    private val _reviewTimeSlotNumber: MutableLiveData<String> = MutableLiveData()
    private val _reviewMaxStudentsProTimeSlot: MutableLiveData<String> = MutableLiveData()

    /**
     * This method returns a review by given id.
     *
     * @param id This is the id of the review that needs to be returned
     * @return [Review] Returns a live data object that contains a review
     */
    fun getReviewById(id: Int): LiveData<Review> {
        _review.value = reviewRepository.getReviewById(id)
        return _review
    }

    /**
     * This method returns the name of a review.
     *
     * @return [String] Returns a live data object containing string
     */
    fun getReviewName(): LiveData<String> {
        _reviewName.value = _review.value!!.name!!
        return _reviewName
    }

    /**
     * This method returns the description of a review.
     *
     * @return [String] Returns a live data object containing string
     */
    fun getReviewInfo(): LiveData<String> {
        _reviewInfo.value = _review.value!!.info!!
        return _reviewInfo
    }

    /**
     * This method returns the number of the signed students for a review.
     *
     * @return [String] Returns a live data object containing string
     */
    fun getSignedStudents(): LiveData<String> {
        var signedStudents = 0
        var maxStudents = 0
        timeSlotRepository.getAllForReview(_review.value!!.id!!)!!.forEach {
            signedStudents += it.currentStudents
            maxStudents += it.maxStudents
        }
        _signedStudents.value = "$signedStudents/$maxStudents"
        return _signedStudents
    }

    /**
     * This method returns the name of a review.
     *
     * @return [String] Returns a live data object containing string
     */
    fun getReviewLocation(): LiveData<String> {
        _reviewLocation.value = _review.value!!.room!!
        return _reviewLocation
    }

    /**
     * This method returns the date of a review.
     *
     * @return [String] Returns a live data object containing string
     */
    fun getReviewDateTime(): LiveData<Timestamp> {
        if (_review.value != null) {
            _reviewDateTime.value = _review.value!!.date!!
        }
        return _reviewDateTime
    }


    /**
     * This method returns the number of the time slots for a review.
     *
     * @return [String] Returns a live data object containing string
     */
    fun getTimeSlotNumber(): LiveData<String> {
        _reviewTimeSlotNumber.value = _review.value!!.numberOfTimeSlots.toString()
        return _reviewTimeSlotNumber
    }

    /**
     * This method returns the maximum number of students for a single time slot for a review.
     *
     * @return [String] Returns a live data object containing string
     */
    fun getMaxStudentsProTimeSlot(): LiveData<String> {
        timeSlotRepository.getAllForReview(_review.value!!.id!!)!!.forEach {
            _reviewMaxStudentsProTimeSlot.value = it.maxStudents.toString()
        }
        return _reviewMaxStudentsProTimeSlot
    }
}