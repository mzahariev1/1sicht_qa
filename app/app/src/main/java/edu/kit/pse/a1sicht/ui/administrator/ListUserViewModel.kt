package edu.kit.pse.a1sicht.ui.administrator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Student
import edu.kit.pse.a1sicht.networking.EmployeeService
import edu.kit.pse.a1sicht.networking.RetrofitClient
import edu.kit.pse.a1sicht.networking.StudentService
import edu.kit.pse.a1sicht.repository.EmployeeRepository
import edu.kit.pse.a1sicht.repository.StudentRepository

/**
 * The ViewModel responsible for all the information that is being used by the ListUserActivity
 * and the functionality within that activity
 *
 * @author Tihomir Georgiev
 */
class ListUserViewModel(application: Application):AndroidViewModel(application){
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
     * These are LiveData objects which contain list of every type of user.
     */
    private var _allEmployees: MutableLiveData<List<Employee>> = MutableLiveData()
    private var _allStudents: MutableLiveData<List<Student>> = MutableLiveData()

    /**
     * This method calls the [EmployeeRepository.getAllEmployees] method, that returns a list
     * with all employee users and stores them into [_allEmployees].
     * @return [_allEmployees] Returns a live data object containing a list with all the employees
     */
    fun getAllEmployees() :LiveData<List<Employee>>{
        _allEmployees.value = employeeRepository.getAllEmployees()
        return _allEmployees
    }

    /**
     * This method calls the [StudentRepository.getAllStudents] method, that returns a list
     * with all student users and stores them into [_allStudents].
     * @return [_allStudents] Returns a live data object containing a list with all the students
     */
    fun getAllStudents() : LiveData<List<Student>>{
        _allStudents.value = studentRepository.getAllStudents()
        return _allStudents
    }

    /**
     * Deletes an employee from the database
     * @param id The id of the employee that is to be deleted
     */
    fun deleteEmployeeById(id: Int) {
        employeeRepository.deleteEmployeeById(id)
    }

    /**
     * Deletes a student from the database
     * @param id The id of the student that is to be deleted
     */
    fun deleteStudentById(id: Int){
        studentRepository.deleteStudentById(id)
    }
}