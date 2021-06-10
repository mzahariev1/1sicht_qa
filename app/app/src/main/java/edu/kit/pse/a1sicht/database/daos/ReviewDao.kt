package edu.kit.pse.a1sicht.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.kit.pse.a1sicht.database.entities.Review

/**
 * The [ReviewDao] class creates a data access object for the [Review] entity.
 *
 * @author Tihomir Georgiev
 */
@Dao
interface ReviewDao {

    /**
     * Inserts a new review in the local database.
     * On conflicts replace the old element with the new one.
     * @param review the review to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(review: Review)

    /**
     * Deletes the data for a review by id from the local database.
     * @param id the id of the deleted review
     */
    @Query("DELETE FROM Reviews WHERE review_id = :id")
    fun deleteById(id: Int)

    /**
     * Deletes the data for all reviews from the local database.
     */
    @Query("DELETE FROM Reviews")
    fun delete()

    /**
     * Updates the data for a review in the local database.
     * @param review the updated review
     */
    @Update
    fun update(review: Review)

    /**
     * Gets all reviews by ids.
     * @param ids array with review's ids
     */
    @Query("SELECT * FROM Reviews WHERE review_id IN (:ids)")
    fun getAllByIds(ids: Array<Int>): LiveData<List<Review>>

    /**
     * Gets a review by id.
     * @param id the id of the review
     */
    @Query("SELECT * FROM Reviews WHERE review_id = :id")
    fun getById(id: Int): LiveData<Review>

    /**
     * Gets all review with the same employee id.
     * @param emp_id the employee's id
     */
    @Query("SELECT * FROM Reviews WHERE employee_id = :emp_id")
    fun getAllByEmployee(emp_id: Int): LiveData<List<Review>>
}