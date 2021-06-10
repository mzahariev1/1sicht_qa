package edu.kit.pse.a1sicht.ui.administrator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.networking.EmployeeService
import edu.kit.pse.a1sicht.networking.RetrofitClient
import edu.kit.pse.a1sicht.repository.EmployeeRepository

/**
 * The class responsible for the information shown on the Requests screen for the admin
 * and the functionality for that screen.
 *
 * @author Tihomir Georgiev, Hristo Klechorov
 */
class ListRequestsViewModel (application: Application):AndroidViewModel(application){
    /**
     * This is an instance of the [EmployeeRepository] class.
     */
    private var employeeRepository: EmployeeRepository = EmployeeRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(EmployeeService::class.java)
    )

    /**
     * These are LiveData objects which contain list of every type of user.
     */
    private var _allUnverifiedEmployees: MutableLiveData<List<Employee>> = MutableLiveData()

    /**
     * This method calls the [EmployeeRepository.getAllEmployees] method, that returns a list
     * with all employee users and stores them into [_allUnverifiedEmployees].
     * @return [_allUnverifiedEmployees] Returns a live data object containing a list with all the employees
     */
    fun getAllUnverifiedEmployees() : LiveData<List<Employee>> {
        _allUnverifiedEmployees.value = employeeRepository.getAllUnverifiedEmployees()
        return _allUnverifiedEmployees
    }

    /**
     * This method verifies employee, that is unverified, by his id.
     * @param id This is the id of the unverified employee
     */
    fun verifyEmployeeById(id:Int){
        employeeRepository.verifyEmployeeById(id)
    }

    /**
     * Deletes the employee when the request is denied     *
     * @param id The id of the employee that will be deleted
     */
    fun deleteEmployeeById(id: Int) {
        employeeRepository.deleteEmployeeById(id)
    }
}