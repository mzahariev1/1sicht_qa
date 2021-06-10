package edu.kit.pse.a1sicht.repository.tasks

import android.os.AsyncTask
import edu.kit.pse.a1sicht.database.daos.ReviewDao
import edu.kit.pse.a1sicht.database.entities.Review

/**
 * This class provides background execution of operations on the local database access object.
 *
 * @author Tihomir Georgiev
 */
class ReviewAsyncTask {

    /**
     * This method executes delete all operation in [ReviewDao] class on background.
     *
     * @param reviewDao This is review data access object from where the operation is called
     */
    fun deleteReview(reviewDao: ReviewDao) {
        DeleteReviewAsyncTask(reviewDao).execute()
    }

    /**
     * This method executes insert operation in [ReviewDao] class on background.
     *
     * @param reviewDao This is review data access object from where the operation is called
     * @param review This is the review that needs to be inserted
     */
    fun insertReview(reviewDao: ReviewDao, review: Review) {
        InsertReviewAsyncTask(reviewDao).execute(review)
    }

    /**
     * This method executes update operation in [ReviewDao] class on background.
     *
     * @param reviewDao This is review data access object from where the operation is called
     * @param review This is the review that needs to be updated
     */
    fun updateReview(reviewDao: ReviewDao, review: Review) {
        UpdateReviewAsyncTask(reviewDao).execute(review)
    }

    /**
     * This method executes delete operation in [ReviewDao] class on background.
     *
     * @param reviewDao This is review data access object from where the operation is called
     * @param id This is the id of the review that needs to be deleted
     */
    fun deleteReviewById(reviewDao: ReviewDao, id: Int) {
        DeleteReviewByIdAsyncTask(reviewDao).execute(id)
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class DeleteReviewByIdAsyncTask constructor(private val reviewDao: ReviewDao) :
        AsyncTask<Int, Void, Void>() {

        override fun doInBackground(vararg id: Int?): Void? {
            reviewDao.deleteById(id[0]!!)
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class DeleteReviewAsyncTask constructor(private val reviewDao: ReviewDao) :
        AsyncTask<Void?, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            reviewDao.delete()
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class InsertReviewAsyncTask constructor(private val reviewDao: ReviewDao) :
        AsyncTask<Review, Void, Void>() {

        override fun doInBackground(vararg review: Review): Void? {
            reviewDao.insert(review[0])
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class UpdateReviewAsyncTask constructor(private val reviewDao: ReviewDao) :
        AsyncTask<Review, Void, Void>() {

        override fun doInBackground(vararg review: Review): Void? {
            reviewDao.update(review[0])
            return null
        }
    }
}