package edu.kit.pse.a1sicht.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.kit.pse.a1sicht.database.entities.Employee

/**
 * The [EmployeeDao] class creates a data access object for the [Employee] entity.
 *
 * @author Tihomir Georgiev
 */
@Dao
interface EmployeeDao {

    /**
     * Inserts a new employee user in the local database.
     * On conflicts replace the old element with the new one.
     * @param employee the employee user to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employee: Employee)

    /**
     * Deletes the data for an employee by id from the local database.
     * @param users_id the id of the deleted employee
     */
    @Query("DELETE FROM Employees WHERE id = :users_id")
    fun deleteById(users_id: Int)

    /**
     * Deletes all entries in the Employees table
     */
    @Query("DELETE FROM Employees")
    fun delete()

    /**
     * Gets the data of the current employee user from the local database.
     * @return the employee user
     */
    @Query("SELECT * FROM Employees")
    fun get(): LiveData<Employee>

    /**
     * Updates the data of the employee user in the local database.
     * @param employee the updated employee user
     */
    @Update
    fun update(employee: Employee)
}