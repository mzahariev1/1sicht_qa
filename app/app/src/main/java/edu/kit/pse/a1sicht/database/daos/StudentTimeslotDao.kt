package edu.kit.pse.a1sicht.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.kit.pse.a1sicht.database.entities.StudentTimeslot

/**
 * The [StudentTimeslotDao] class creates a data access object for the [StudentTimeslot] entity.
 *
 * @author Tihomir Georgiev
 */
@Dao
interface StudentTimeslotDao {

    /**
     * Inserts a new timeslot in the local database.
     * @param [std_timeslot] The StudentTimeslot entry to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(std_timeslot: StudentTimeslot)

    /**
     * Deletes a given entry from the local database
     */
    @Delete
    fun delete(std_timeslot: StudentTimeslot)

    /**
     * Gets all time slot's ids for one student
     * @param student_id The id of the student
     * @return Live data object containing a list with time slots' ids
     */
    @Query("SELECT timeslot_id FROM StudentTimeslot WHERE student_id = :student_id")
    fun getAllTimeSlotIdsForStudent(student_id: Int): LiveData<List<Int>>

    /**
     * Gets all student's ids for a specific time slot
     * @param timeSlot_id The id of the time slot
     * @return Live data object containing a list with students' ids
     */
    @Query("SELECT student_id FROM StudentTimeslot WHERE timeslot_id = :timeSlot_id")
    fun getAllStudentIdsForTimeSlot(timeSlot_id: Int): LiveData<List<Int>>
}