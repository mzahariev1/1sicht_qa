package edu.kit.pse.a1sicht.repository

import androidx.lifecycle.LiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.networking.AdministratorService
import edu.kit.pse.a1sicht.database.entities.Administrator
import edu.kit.pse.a1sicht.repository.tasks.AdministratorAsyncTask
import edu.kit.pse.a1sicht.repository.tasks.EmployeeAsyncTask
import edu.kit.pse.a1sicht.repository.tasks.StudentAsyncTask


/**
 * The [AdministratorRepository] class gives read and write access to Administrator objects to the local and server database
 *
 * @author Maximilian Ackermann, Stanimir Bozhilov, Martin Zahariev
 */
class AdministratorRepository constructor(
    localDatabase: LocalDatabase,
    private val adminService: AdministratorService
) {

    private val adminDao = localDatabase.adminDao()
    private val employeeDao = localDatabase.employeeDao()
    private val studentDao = localDatabase.studentDao()

    private val employeeAsyncTask: EmployeeAsyncTask = EmployeeAsyncTask()
    private val administratorAsyncTask: AdministratorAsyncTask = AdministratorAsyncTask()
    private val studentAsyncTask: StudentAsyncTask = StudentAsyncTask()

    /**
     * Creates an new Administrator object.
     * Inserts the Administrator via Dao and Service classes into local and remote databases.
     * @param [googleID] A String object representing the unique Google ID of the administrator.
     * @param [firstName] A String object representing the first name of the administrator.
     * @param [lastName] A String object representing the last name of the administrator.
     * @return [Administrator] The newly created Administrator
     */
    fun createAdmin(googleID: String, firstName: String, lastName: String): Administrator? {

        administratorAsyncTask.deleteAdministrator(adminDao)
        employeeAsyncTask.deleteEmployee(employeeDao)
        studentAsyncTask.deleteStudent(studentDao)

        val newAdmin =
            adminService.create(Administrator(null, googleID, firstName, lastName)).blockingFirst()

        administratorAsyncTask.insertAdministrator(adminDao, newAdmin)

        return newAdmin
    }

    /**
     * This method gets the administrator from the local database
     */
    fun getAdministrator(): LiveData<Administrator> {
        return adminDao.get()
    }

    /**
     * This method inserts the administrator in the local database
     * @param [admin] The admin which is to be inserted in the local database
     */
    fun insertAdmin(admin: Administrator) {
        administratorAsyncTask.insertAdministrator(adminDao, admin)
    }

    /**
     * This method deletes the admin from the local database
     */
    fun deleteAdmin() {
        administratorAsyncTask.deleteAdministrator(adminDao)
    }

    /**
     * This method gets all administrators from the remote database
     * @return [List<Administrator>] containing all administrators on the server.
     */
    fun getAllAdmins(): List<Administrator>? {
        return try {
            adminService.getAll().blockingFirst()
        } catch (error: Exception) {
            //return an empty list on error
            listOf()
        }
    }

    /**
     * This method gets an administrator by a given ID number from the remote database
     * @param [adminId] The unique ID number of the administrator
     * @return [Administrator]
     */
    fun getAdminById(adminId: Int): Administrator? {
        return getAllAdmins()?.firstOrNull { it.id == adminId }
    }

    /**
     * This method updates an administrator by a given ID number
     * @param [adminId] The unique ID number of the administrator which is to be updated
     * @param [admin] An Administrator object holding the new data.
     * @return [Administrator] The updated Administrator.
     */
    fun updateAdminById(adminId: Int, admin: Administrator): Administrator {
        val updatedAdmin = adminService.updateById(adminId, admin).blockingFirst()
        administratorAsyncTask.updateAdministrator(adminDao, admin)
        return updatedAdmin
    }

    /**
     * Deletes an Administrator that has the given [adminId].
     * @param [adminId] The unique ID number of the administrator which is to be deleted
     * @return [Administrator] The deleted Administrator
     */
    fun deleteAdminById(adminId: Int): Administrator? {
        return if (adminDao.get().value?.id == adminId) {
            null
        } else {
            adminService.deleteById(adminId).blockingFirst()
        }
    }
}