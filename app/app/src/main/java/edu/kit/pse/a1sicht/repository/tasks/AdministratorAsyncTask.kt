package edu.kit.pse.a1sicht.repository.tasks

import android.os.AsyncTask
import edu.kit.pse.a1sicht.database.daos.AdministratorDao
import edu.kit.pse.a1sicht.database.entities.Administrator

/**
 * This class provides background execution of operations on the local database access object.
 *
 * @author Tihomir Georgiev
 */
class AdministratorAsyncTask {

    /**
     * This method executes delete all operation in [AdministratorDao] class on background.
     *
     * @param adminDao This is administrator data access object from where the operation is called
     */
    fun deleteAdministrator(adminDao: AdministratorDao) {
        DeleteAdministratorAsyncTask(adminDao).execute()
    }

    /**
     * This method executes insert operation in [AdministratorDao] class on background.
     *
     * @param adminDao This is administrator data access object from where the operation is called
     * @param administrator This is the administrator that needs to be inserted
     */
    fun insertAdministrator(adminDao: AdministratorDao, administrator: Administrator) {
        InsertAdministratorAsyncTask(adminDao).execute(administrator)
    }

    /**
     * This method executes update operation in [AdministratorDao] class on background.
     *
     * @param adminDao This is administrator data access object from where the operation is called
     * @param administrator This is the administrator that needs to be updated
     */
    fun updateAdministrator(adminDao: AdministratorDao, administrator: Administrator){
        UpdateAdministratorAsyncTask(adminDao).execute(administrator)
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class DeleteAdministratorAsyncTask constructor(private val adminDao: AdministratorDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            adminDao.delete()
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class InsertAdministratorAsyncTask constructor(private val adminDao: AdministratorDao) :
        AsyncTask<Administrator, Void, Void>() {

        override fun doInBackground(vararg administrator: Administrator): Void? {
            adminDao.insert(administrator[0])
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class UpdateAdministratorAsyncTask constructor(private val adminDao: AdministratorDao) :
        AsyncTask<Administrator, Void, Void>() {

        override fun doInBackground(vararg administrator: Administrator): Void? {
            adminDao.update(administrator[0])
            return null
        }
    }
}