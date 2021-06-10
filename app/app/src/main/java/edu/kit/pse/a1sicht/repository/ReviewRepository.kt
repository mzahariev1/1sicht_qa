package edu.kit.pse.a1sicht.repository

import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.networking.ReviewService
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.networking.StudentService
import edu.kit.pse.a1sicht.repository.tasks.ReviewAsyncTask
import java.sql.Timestamp
import java.util.*

/**
 * The [ReviewRepository] class gives read and write access to Review objects to the local and server database
 *
 * @author Maximilian Ackermann, Stanimir Bozhilov, Martin Zahariev
 */
@Suppress("DEPRECATION")
class ReviewRepository constructor(
    database: LocalDatabase, private val reviewService: ReviewService,
    private val studentService: StudentService
) {

    private val reviewDao = database.reviewDao()

    private val reviewAsyncTask: ReviewAsyncTask = ReviewAsyncTask()

    /**
     * Creates a new Review object.
     * Inserts the Review via Dao and service classes into the local and server database.
     * @param [name] A String object representing the name of the review
     * @param [room] A String object representing the room in which the review is going to be held
     * @param [year] An Integer representing the year of the review
     * @param [month] An Integer representing the month of the review
     * @param [day] An Integer representing the day of the review
     * @param [hour] An Integer representing the hour of the review
     * @param [minute] An Integer representing the minute of the review
     * @param [timeslotLenght] An Integer representing the length of the timeslots in the review
     * @param [numberOfTimeslots] An Integer representing the number of timeslots in the review
     * @param [employeeId] An Integer representing the unique ID number of the creator of the review
     * @param [info] A String object holding description and additional info for the review
     * @return [Review] The newly created review
     */
    fun createReview(
        name: String, room: String,
        year: Int, month: Int, day: Int, hour: Int, minute: Int, timeslotLenght: Int,
        numberOfTimeslots: Int, employeeId: Int, info: String
    ): Review? {

        var newReview = Review(
            null, name, room, Timestamp(year - 1900, month, day, hour, minute, 0, 0),
            timeslotLenght, numberOfTimeslots, employeeId, info
        )
        newReview = reviewService.create(newReview).blockingFirst()
        reviewAsyncTask.insertReview(reviewDao, newReview)
        return newReview
    }

    /**
     * This method gets all reviews from the remote database.
     * @return [List<Review>] containing all reviews on the server.
     */
    fun getAllReviews(): List<Review>? {
        reviewAsyncTask.deleteReview(reviewDao)
        var reviewList: List<Review> = ArrayList()
        try {
            reviewList = reviewService.getAll().blockingFirst()
        } catch (e: Exception) {
            //continue
        }
        reviewList.forEach {
            reviewAsyncTask.insertReview(reviewDao, it)
        }
        return reviewList
    }

    /**
     * This method gets a review by a given ID number from the remote database
     * @param [reviewId] The unique ID number of the review
     * @return [Review] with the given ID number
     */
    fun getReviewById(reviewId: Int): Review? {
        val review = getAllReviews()?.firstOrNull { it.id == reviewId }
        if (reviewDao.getById(reviewId).value == null) {
          //  reviewAsyncTask.insertReview(reviewDao, review!!)
        }
        return review
    }

    /**
     * This method gets all reviews by a set of keywords
     * @return [List<Review>] containing all reviews which match the search query
     */
    fun getReviewsByKeywords(keywords: String): List<Review>? {
        return reviewService.getByKeywords(keywords).blockingFirst()
    }

    /**
     * This method updates a review by a given ID number.
     * @param [reviewId] The unique ID number of the review
     * @param [review] A Review object holding the new data
     * @return [Review] The updated Review object
     */
    fun updateReviewById(reviewId: Int, review: Review): Review? {
        reviewAsyncTask.updateReview(reviewDao, review)
        return reviewService.updateById(reviewId, review).blockingFirst()
    }

    /**
     * This method deletes a review by a given ID number
     * @param [reviewId] The unique ID number of the review
     * @return [Review] The deleted review
     */
    fun deleteReviewById(reviewId: Int): Review? {
        if (reviewDao.getById(reviewId).value != null) {
            reviewAsyncTask.deleteReviewById(reviewDao, reviewId)
        }

        return reviewService.deleteById(reviewId).blockingFirst()
    }

    /**
     * This method gets all reviews which have been created by a employee with a given ID number
     * @param [employeeId] The unique ID number of the employee
     * @return [List<Review>] containing all reviews which have been created by the given employee
     */
    fun getAllReviewsByEmployee(employeeId: Int): List<Review>? {
        reviewAsyncTask.deleteReview(reviewDao)
        var reviewList: List<Review> = ArrayList()
        try {
            reviewList = reviewService.getAll().blockingFirst()!!.filter { it.employee_id == employeeId }
        } catch (e: Exception) {
            //continue
        }
        reviewList.forEach {
            reviewAsyncTask.insertReview(reviewDao, it)
        }
        return reviewList
    }

    /**
     * This method gets all reviews for which a given student has signed up
     * @param [studentId] The unique ID number of the student
     * @return [List<Review>] containing all reviews for which the given student has signed up
     */
    fun getReviewsForStudent(studentId: Int): List<Review>? {
        reviewAsyncTask.deleteReview(reviewDao)

        var reviewList: List<Review> = ArrayList()
        try {
            reviewList = studentService.getReviews(studentId).blockingFirst()
        } catch (e: Exception) {
            //continue
        }
        reviewList.forEach {
            reviewAsyncTask.insertReview(reviewDao, it)
        }
        return reviewList
    }
}

