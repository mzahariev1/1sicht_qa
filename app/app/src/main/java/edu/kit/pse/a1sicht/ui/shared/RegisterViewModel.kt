package edu.kit.pse.a1sicht.ui.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.networking.*
import edu.kit.pse.a1sicht.repository.AdministratorRepository
import edu.kit.pse.a1sicht.repository.EmployeeRepository
import edu.kit.pse.a1sicht.repository.StudentRepository

/**
 * This is a view model class that implements [AndroidViewModel], which stores and manages the data
 * for the register activity.
 *
 * @author Tihomir Georgiev
 * @see RegisterActivity
 * @see EmployeeRepository
 * @see StudentRepository
 */
class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * This is a instance of the [EmployeeRepository] class.
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
     * This method call the [EmployeeRepository.createEmployee] method for the register of a new
     * employee user.
     *
     * @param googleId This is the google id of the user
     * @param firstName This is the first name of the user
     * @param lastName This is the last name of the user
     */
    fun createEmployee(googleId: String, firstName: String, lastName: String) {
        employeeRepository.createEmployee(googleId, firstName, lastName)
    }

    /**
     * This method call the [StudentRepository.createStudent] method for the register of a new
     * student user.
     *
     * @param googleId This is the google id of the user
     * @param firstName This is the first name of the user
     * @param lastName This is the last name of the user
     * @param matriculationNumber This is the matriculation number of the student
     */
    fun createStudent(googleId: String, firstName: String, lastName: String, matriculationNumber: Int){
        studentRepository.createStudent(googleId,firstName,lastName,matriculationNumber)
    }
}