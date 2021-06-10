package edu.kit.pse.a1sicht.ui.administrator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.database.entities.Timeslot
import edu.kit.pse.a1sicht.networking.RetrofitClient
import edu.kit.pse.a1sicht.networking.ReviewService
import edu.kit.pse.a1sicht.networking.StudentService
import edu.kit.pse.a1sicht.repository.ReviewRepository
import edu.kit.pse.a1sicht.repository.TimeslotRepository

/**
 * The ReviewModel responsible for all the information being displayed in the ListReviewActivity.
 *
 * @author Tihomir Georgiev
 */
class ListReviewViewModel(application: Application) : AndroidViewModel(application) {

    private var reviewRepository: ReviewRepository = ReviewRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(ReviewService::class.java),
        RetrofitClient.getInstance()!!.create(StudentService::class.java)
    )

    private var timeSlotRepository = TimeslotRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(ReviewService::class.java),
        RetrofitClient.getInstance()!!.create(StudentService::class.java)
    )

    // All reviews as MutableLiveData
    private var _allReviews: MutableLiveData<List<Review>> = MutableLiveData()
    private var _allTimeSlots: MutableLiveData<List<Timeslot>> = MutableLiveData()

    /**
     * Gets all reviews
     * @return All the reviews
     */
    fun getAllReviews(): LiveData<List<Review>> {
        _allReviews.value = reviewRepository.getAllReviews()
        return _allReviews
    }

    /**
     * Deletes a review by it's id
     * @param id The id of the review that is to be deleted
     */
    fun deleteReviewById(id: Int) {
        timeSlotRepository.getAllForReview(id)!!.forEach {
            timeSlotRepository.deleteById(id, it.id!!)
        }
        reviewRepository.deleteReviewById(id)
    }

    fun getAllReviewsForStudent(studentId: Int): LiveData<List<Review>> {
        _allReviews.value = reviewRepository.getAllReviews()
        _allTimeSlots.value = timeSlotRepository.getAllForStudent(studentId)
        val list: MutableList<Review> = ArrayList()
        timeSlotRepository.getAllForStudent(studentId)!!.forEach {
            list.add(reviewRepository.getReviewById(it.belongsTo)!!)
        }
        _allReviews.value = list
        return _allReviews
    }

    fun getAllUnsignedReviews(studentId: Int): LiveData<List<Review>> {
        _allReviews.value = reviewRepository.getAllReviews()
        _allTimeSlots.value = timeSlotRepository.getAllForStudent(studentId)
        val list: MutableList<Review> = reviewRepository.getAllReviews()!!.toMutableList()
        timeSlotRepository.getAllForStudent(studentId)!!.forEach {
            list.remove(reviewRepository.getReviewById(it.belongsTo)!!)
        }
        _allReviews.value = list
        return _allReviews
    }
}