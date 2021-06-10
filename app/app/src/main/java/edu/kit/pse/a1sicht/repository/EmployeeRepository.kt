package edu.kit.pse.a1sicht.repository

import androidx.lifecycle.LiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.networking.EmployeeService
import edu.kit.pse.a1sicht.repository.tasks.AdministratorAsyncTask
import edu.kit.pse.a1sicht.repository.tasks.EmployeeAsyncTask
import edu.kit.pse.a1sicht.repository.tasks.StudentAsyncTask
import java.lang.Exception

/**
 * The [EmployeeRepository] class gives read and write access to Employee objects to the local and server database
 *
 * @author Maximilian Ackermann, Stanimir Bozhilov, Martin Zahariev
 */
class EmployeeRepository constructor(localDatabase: LocalDatabase, private val employeeService: EmployeeService) {

    private val employeeDao = localDatabase.employeeDao()
    private val adminDao = localDatabase.adminDao()
    private val studentDao = localDatabase.studentDao()

    private val employeeAsyncTask: EmployeeAsyncTask = EmployeeAsyncTask()
    private val administratorAsyncTask: AdministratorAsyncTask = AdministratorAsyncTask()
    private val studentAsyncTask: StudentAsyncTask = StudentAsyncTask()

    /**
     * Creates a new Employee object.
     * Inserts the Employee via Dao and service classes into the local and server database.
     * @param [googleID] A String object representing the unique Google ID of the employee.
     * @param [firstName] A String object representing the first name of the employee.
     * @param [lastName] A String object representing the last name of the employee.
     * @return [Employee] The newly created Employee
     */
    fun createEmployee(googleID: String, firstName: String, lastName: String): Employee? {
        administratorAsyncTask.deleteAdministrator(adminDao)
        employeeAsyncTask.deleteEmployee(employeeDao)
        studentAsyncTask.deleteStudent(studentDao)

        var newEmployee = Employee(null, googleID, firstName, lastName, false)
        newEmployee = employeeService.createEmployee(newEmployee).blockingFirst()
        employeeAsyncTask.insertEmployee(employeeDao, newEmployee)

        return newEmployee
    }

    /**
     * This method inserts an employee in the local database
     * @param [employee] The Employee to be inserted
     */
    fun insertEmployee(employee: Employee) {
        employeeAsyncTask.insertEmployee(employeeDao, employee)
    }

    /**
     * This method gets the employee from the local database
     */
    fun getEmployee(): LiveData<Employee> {
        return employeeDao.get()
    }

    /**
     * This method deletes the employee from the local database
     */
    fun deleteEmployee() {
        employeeAsyncTask.deleteEmployee(employeeDao)
    }

    /**
     * This method gets all employees from the remote database
     * @return [List<Employee>] containing all employees on the server.
     */
    fun getAllEmployees(): List<Employee>? {
        return try {
            employeeService.getAll().blockingFirst()
        } catch (error: Exception) {
            //Return empty list on error
            listOf()
        }
    }

    /**
     * This method gets all unverified employees from the remote database
     * @return [List<Employee>] containing all unverified employees
     */
    fun getAllUnverifiedEmployees(): List<Employee>? {
        return getAllEmployees()?.filter { !it.verified }
    }

    /**
     * This method gets an employee by a given ID number from the remote database
     * @param [employeeId] The unique ID number of the employee
     * @return [Employee] with the given ID number
     */
    fun getEmployeeById(employeeId: Int): Employee? {
        return getAllEmployees()?.firstOrNull { it.id == employeeId }
    }

    /**
     * This method gets the creator of a review with a given ID number
     * @param [reviewId] The ID number of the review
     * @return [Employee] The creator of the review.
     */
    fun getEmployeeByReview(reviewId: Int): Employee? {
        return employeeService.getEmployeeByReview(reviewId).blockingFirst()
    }

    /**
     * This method updates an employee by a given ID number
     * @param [employeeId] The unique ID number of the employee which is to be updated
     * @param [employee] An Employee object holding the new data.
     * @return [Employee] The updated Employee.
     */
    fun updateEmployeeById(employeeId: Int, employee: Employee): Employee? {
        val updatedEmployee = employeeService.updateById(employeeId, employee).blockingFirst()
        employeeAsyncTask.updateEmployee(employeeDao, employee)
        return updatedEmployee
    }

    /**
     * Deletes an Employee that has the given [employeeId].
     * @param [employeeId] The unique ID number of the employee which is to be deleted
     * @return [Employee] The deleted Employee
     */
    fun deleteEmployeeById(employeeId: Int): Employee? {
        return if (employeeDao.get().value?.id == employeeId) {
            null
        } else {
            employeeService.deleteById(employeeId).blockingFirst()
        }
    }

    /**
     * This method verifies an employee
     * @param [employeeId] The unique ID number of the employee which is to be verified
     */
    fun verifyEmployeeById(employeeId: Int) {
        employeeService.verifyById(employeeId).execute()
    }

    /**
     * This method checks whether an employee with a given ID number has been verified
     * @param [employeeId] The unique ID number of the employee
     * @return [true] if the employee has already been verified, [false] otherwise
     */
    fun isVerified(employeeId: Int): Boolean {
        return employeeService.getById(employeeId).blockingFirst().verified
    }
}
