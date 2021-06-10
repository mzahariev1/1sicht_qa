package edu.kit.pse.a1sicht.ui.shared

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
 * for the log in activity.
 *
 * @author Tihomir Georgiev
 * @see LogInActivity
 * @see EmployeeRepository
 * @see StudentRepository
 * @see AdministratorRepository
 */
class LogInViewModel(application: Application) : AndroidViewModel(application) {
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
     * These are LiveData objects which contain list of every type of user.
     */
    private var _allEmployees: MutableLiveData<List<Employee>> = MutableLiveData()
    private var _allStudents: MutableLiveData<List<Student>> = MutableLiveData()
    private var _allAdministrators: MutableLiveData<List<Administrator>> = MutableLiveData()

    /**
     * This method calls the [EmployeeRepository.getAllEmployees] method, that returns a list
     * with all employee users and stores them into [_allEmployees].
     */
    private fun getAllEmployees() {
        _allEmployees.value = employeeRepository.getAllEmployees()
    }

    /**
     * This method calls the [StudentRepository.getAllStudents] method, that returns a list
     * with all student users and stores them into [_allStudents].
     */
    private fun getAllStudents() {
        _allStudents.value = studentRepository.getAllStudents()
    }

    /**
     * This method calls the [AdministratorRepository.getAllAdmins] method, that returns a list
     * with all student users and stores them into [_allAdministrators].
     */
    private fun getAllAdministrators() {
        _allAdministrators.value = adminRepository.getAllAdmins()
    }

    /**
     * This method defines by given google id to which type of user is the currently logged in
     * user(student/employee/administrator).
     *
     * @param googleId This is the google id of a user
     * @return 1 If the user is a student
     * @return 2 If the user is a verified employee
     * @return 3 If the user is an administrator
     * @return 4 If the user is an unverified employee
     * @return 0 If the user is not registered yet
     * @return -1 If the there is no connection to the server
     */
    fun defineUser(googleId: String): Int {
        getAllEmployees()
        getAllAdministrators()
        getAllStudents()

        if(_allAdministrators.value.isNullOrEmpty() && _allEmployees.value.isNullOrEmpty() && _allStudents.value.isNullOrEmpty()){
            return -1
        }
        _allStudents.value!!.forEach {
            if (it.googleID.equals(googleId)) {
                //Insert the student in the local database.
                studentRepository.insertStudent(it)
                return 1
            }
        }
        _allEmployees.value!!.forEach {
            if (it.googleID.equals(googleId)) {
                return if (!it.verified) {
                    4
                } else {
                    //Insert the employee in the local database.
                    employeeRepository.insertEmployee(it)
                    2
                }
            }
        }
        _allAdministrators.value!!.forEach {
            Log.e(googleId,it.googleID)
            if (it.googleID.equals(googleId)) {
                //Insert the administrator in the local database.
                adminRepository.insertAdmin(it)
                return 3
            }
        }
        return 0
    }
}