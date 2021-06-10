package edu.kit.pse.a1sicht.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.kit.pse.a1sicht.database.entities.Timeslot

/**
 * The [TimeslotDao] class creates a data access object for the [Timeslot] entity.
 *
 * @author Tihomir Georgiev
 */
@Dao
interface TimeslotDao {

    /**
     * Inserts a new time slot in the local database.
     * On conflicts replace the old element with the new one.
     * @param timeslot the time slot to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(timeslot: Timeslot)

    /**
     * Deletes the data for time slot by id from the local database.
     * @param id the id of the deleted time slot
     */
    @Query("DELETE FROM TimeSlots WHERE id = :id")
    fun deleteById(id: Int)

    /**
     * Deletes the data for time slots from the local database.
     */
    @Query("DELETE FROM TimeSlots")
    fun delete()

    /**
     * Updates the data for a time slot in the local database.
     * @param timeslot the updated time slot
     */
    @Update
    fun update(timeslot: Timeslot)

    /**
     * Gets a time slot by id.
     * @param id the time slot's id
     */
    @Query("SELECT * FROM TimeSlots WHERE id = :id")
    fun getById(id: Int): LiveData<Timeslot>

    /**
     * Gets all time slots with the same review's id.
     * @param rev_id the review's id
     */
    @Query("SELECT * FROM TimeSlots WHERE belongs_To = :rev_id")
    fun getAllByReview(rev_id: Int): LiveData<List<Timeslot>>

}