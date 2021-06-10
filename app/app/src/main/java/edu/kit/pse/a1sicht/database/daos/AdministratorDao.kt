package edu.kit.pse.a1sicht.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.kit.pse.a1sicht.database.entities.Administrator

/**
 * The [AdministratorDao] class creates a data access object for the [Administrator] entity.
 *
 * @author Tihomir Georgiev
 */
@Dao
interface AdministratorDao {

    /**
     * Inserts a new administrator user in the local database.
     * On conflicts replace the old element with the new one.
     * @param administrator the administrator user to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(administrator: Administrator)

    /**
     * Deletes the data for an administrator by id from the local database.
     * @param users_id the id of the deleted administrator
     */
    @Query("DELETE FROM Admins WHERE id = :users_id")
    fun deleteById(users_id: Int)

    /**
     * Deletes all entries in the administrator table
     */
    @Query("DELETE FROM Admins")
    fun delete()

    /**
     * Gets the data of the current administrator user from the local database.
     * @return administrator user
     */
    @Query("SELECT * FROM Admins")
    fun get(): LiveData<Administrator>

    /**
     * Updates the data of the administrator user in the local database.
     * @param administrator the updated administrator user
     */
    @Update
    fun update(administrator: Administrator)

}