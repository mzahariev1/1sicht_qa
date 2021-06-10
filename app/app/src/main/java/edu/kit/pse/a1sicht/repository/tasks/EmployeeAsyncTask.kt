package edu.kit.pse.a1sicht.repository.tasks

import android.os.AsyncTask
import edu.kit.pse.a1sicht.database.daos.EmployeeDao
import edu.kit.pse.a1sicht.database.entities.Employee

/**
 * This class provides background execution of operations on the local database access object.
 *
 * @author Tihomir Georgiev
 */
class EmployeeAsyncTask {

    /**
     * This method executes delete all operation in [EmployeeDao] class on background.
     *
     * @param employeeDao This is employee data access object from where the operation is called
     */
    fun deleteEmployee(employeeDao: EmployeeDao){
        DeleteEmployeeAsyncTask(employeeDao).execute()
    }

    /**
     * This method executes insert operation in [EmployeeDao] class on background.
     *
     * @param employeeDao This is employee data access object from where the operation is called
     * @param employee This is the employee that needs to be inserted
     */
    fun insertEmployee(employeeDao: EmployeeDao,employee: Employee){
        InsertEmployeeAsyncTask(employeeDao).execute(employee)
    }

    /**
     * This method executes update operation in [EmployeeDao] class on background.
     *
     * @param employeeDao This is employee data access object from where the operation is called
     * @param employee This is the employee that needs to be updated
     */
    fun updateEmployee(employeeDao: EmployeeDao,employee: Employee){
        UpdateEmployeeAsyncTask(employeeDao).execute(employee)
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class DeleteEmployeeAsyncTask constructor(private val employeeDao: EmployeeDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            employeeDao.delete()
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class InsertEmployeeAsyncTask constructor(private val employeeDao: EmployeeDao) :
        AsyncTask<Employee, Void, Void>() {

        override fun doInBackground(vararg employee: Employee): Void? {
            employeeDao.insert(employee[0])
            return null
        }
    }

    /**
     * This is private class that implements [AsyncTask] class for background execution.
     */
    private class UpdateEmployeeAsyncTask constructor(private val employeeDao: EmployeeDao) :
        AsyncTask<Employee, Void, Void>() {

        override fun doInBackground(vararg employee: Employee): Void? {
            employeeDao.update(employee[0])
            return null
        }
    }
}