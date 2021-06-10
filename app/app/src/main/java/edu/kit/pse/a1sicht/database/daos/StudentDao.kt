package edu.kit.pse.a1sicht.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.kit.pse.a1sicht.database.entities.Student

/**
 * The [StudentDao] class creates a data access object for the [Student] entity.
 *
 * @author Tihomir Georgiev
 */
@Dao
interface StudentDao {

    /**
     * Inserts a new student user in the local database.
     * On conflicts replace the old element with the new one.
     * @param student the student user to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(student: Student)

    /**
     * Deletes the data for a student by id from the local database.
     * @param users_id the id of the deleted student
     */
    @Query("DELETE FROM Students WHERE id = :users_id")
    fun deleteById(users_id: Int)

    /**
     * Deletes all entries from the Student table
     */
    @Query("DELETE FROM Students")
    fun delete()

    /**
     * Gets the data of the current student user from the local database.
     * @return the student user
     */
    @Query("SELECT * FROM Students")
    fun get(): LiveData<Student>

    /**
     * Updates the data of the student user in the local database.
     * @param student the updated employee user
     */
    @Update
    fun update(student: Student)
}