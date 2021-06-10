package edu.kit.pse.a1sicht.ui.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.Administrator
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Student
import edu.kit.pse.a1sicht.networking.*
import edu.kit.pse.a1sicht.repository.AdministratorRepository
import edu.kit.pse.a1sicht.repository.EmployeeRepository
import edu.kit.pse.a1sicht.repository.StudentRepository

/**
 * This is a view model class that implements [AndroidViewModel], which stores and manages the data
 * for the settings activity.
 *
 * @author Tihomir Georgiev
 *
 * @see SettingsActivity
 * @see EmployeeRepository
 * @see StudentRepository
 * @see AdministratorRepository
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * This is an instance of the [EmployeeRepository] class.
     */
    private var employeeRepository: EmployeeRepository = EmployeeRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(EmployeeService::class.java)
    )
    /**
     * This is an instance of the [StudentRepository] class.
     */
    private var studentRepository: StudentRepository = StudentRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(StudentService::class.java)
    )
    /**
     * This is an instance of the [AdministratorRepository] class.
     */
    private var adminRepository: AdministratorRepository = AdministratorRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(AdministratorService::class.java)
    )

    /**
     * These are LiveData objects which contain every type of user each.
     */
    private var _employee: LiveData<Employee> = employeeRepository.getEmployee()
    private var _student: LiveData<Student> = studentRepository.getStudent()
    private var _administrator: LiveData<Administrator> = adminRepository.getAdministrator()

    /**
     * This method gets the current logged in employee user.
     * @return Live data object containing employee type
     */
    fun getEmployeeUser(): LiveData<Employee> {
        return _employee
    }

    /**
     * This method gets the current logged in administrator user.
     * @return Live data object containing administrator type
     */
    fun getAdministratorUser(): LiveData<Administrator> {
        return _administrator
    }

    /**
     * This method gets the current logged in student user.
     * @return Live data object containing student type
     */
    fun getStudentUser(): LiveData<Student> {
        return _student
    }

    /**
     * This method changes the last name of the current logged in user.
     * @param lastName The new last name of the user
     * @param userType Integer to determine which type of user is currently logged in
     */
    fun setLastName(lastName: String, userType: Int) {
        when (userType) {
            1 -> {
                _employee.value!!.lastName = lastName
                employeeRepository.updateEmployeeById(_employee.value!!.id!!, _employee.value!!)
            }
            2 -> {
                _student.value!!.lastName = lastName
                studentRepository.updateStudentById(_student.value!!.id, _student.value!!)
            }
            3 -> {
                _administrator.value!!.lastName = lastName
                adminRepository.updateAdminById(_administrator.value!!.id!!, _administrator.value!!)
            }
        }
    }

    /**
     * This method changes the first name of the current logged in user.
     * @param firstName The new first name of the user
     * @param userType Integer to determine which type of user is currently logged in
     */
    fun setFirstName(firstName: String, userType: Int) {
        when (userType) {
            1 -> {
                _employee.value!!.firstName = firstName
                employeeRepository.updateEmployeeById(_employee.value!!.id!!, _employee.value!!)
            }
            2 -> {
                _student.value!!.firstName = firstName
                studentRepository.updateStudentById(_student.value!!.id, _student.value!!)
            }
            3 -> {
                _administrator.value!!.firstName = firstName
                adminRepository.updateAdminById(_administrator.value!!.id!!, _administrator.value!!)
            }
        }
    }

    /**
     * This method changes the matriculation number of currently logged in student user.
     * @param mNumber The new matriculation number
     * @return [Boolean] True if the operation was successful, false if it wasn't
     */
    fun setMatriculationNumber(mNumber: Int): Boolean {
        _student.value!!.matriculationNumber = mNumber
        return studentRepository.updateStudentById(_student.value!!.id, _student.value!!)
    }

    /**
     * This method deletes the currently logged in user and his information from the local database.
     */
    fun deleteUserInformation(){
        employeeRepository.deleteEmployee()
        studentRepository.deleteStudent()
        adminRepository.deleteAdmin()
    }

    fun deleteUser(userType: Int){
        when (userType) {
            1 -> employeeRepository.deleteEmployeeById(_employee.value!!.id!!)
            2 -> studentRepository.deleteStudentById(_student.value!!.id)
            3 -> adminRepository.deleteAdminById(_administrator.value!!.id!!)
        }
        deleteUserInformation()
    }
}