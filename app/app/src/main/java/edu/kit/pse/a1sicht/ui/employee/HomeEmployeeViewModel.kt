package edu.kit.pse.a1sicht.ui.employee

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.kit.pse.a1sicht.database.LocalDatabase
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.networking.EmployeeService
import edu.kit.pse.a1sicht.networking.RetrofitClient
import edu.kit.pse.a1sicht.networking.ReviewService
import edu.kit.pse.a1sicht.networking.StudentService
import edu.kit.pse.a1sicht.repository.EmployeeRepository
import edu.kit.pse.a1sicht.repository.ReviewRepository
import edu.kit.pse.a1sicht.repository.TimeslotRepository

/**
 * This is a view model class that implements [AndroidViewModel] class and
 * stores and manages information for the [HomeEmployeeActivity].
 *
 * @author Tihomir Georgiev
 * @see [EmployeeRepository]
 * @see [HomeEmployeeActivity]
 * @see [ReviewRepository]
 * @see [TimeslotRepository]
 */
class HomeEmployeeViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * This is a variable of the [EmployeeRepository] class.
     */
    private var employeeRepository: EmployeeRepository = EmployeeRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(EmployeeService::class.java)
    )

    /**
     * This is a variable of the [TimeslotRepository] class.
     */
    private var timeslotRepository = TimeslotRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(ReviewService::class.java),
        RetrofitClient.getInstance()!!.create(StudentService::class.java)
    )

    /**
     * This is a variable of the [ReviewRepository] class.
     */
    private var reviewRepository: ReviewRepository = ReviewRepository(
        LocalDatabase.getInstance(application),
        RetrofitClient.getInstance()!!.create(ReviewService::class.java),
        RetrofitClient.getInstance()!!.create(StudentService::class.java)
    )

    /**
     * Local variables
     */
    private var _employee: LiveData<Employee> = employeeRepository.getEmployee()
    private var _allReviews: MutableLiveData<List<Review>> = MutableLiveData()

    /**
     * This method returns all reviews of one employee.
     *
     * @param id This is the id of the employee
     * @return Returns a live data object containing a list with reviews
     */
    fun getAllReviewsForEmployee(id: Int): LiveData<List<Review>> {
        _allReviews.value = reviewRepository.getAllReviewsByEmployee(id)
        return _allReviews
    }

    /**
     * This method returns the current logged in employee user.
     *
     * @return [Employee] Returns a live data object containing an employee
     */
    fun getEmployee(): LiveData<Employee> {
        _employee = employeeRepository.getEmployee()
        return _employee
    }

    /**
     * This method deletes a review by given id and the time slots that belong to this
     * review by given id.
     *
     * @param id This is the id of the review that needed to be deleted
     */
    fun deleteReviewById(id: Int) {
        timeslotRepository.getAllForReview(id)!!.forEach {
            timeslotRepository.deleteById(id, it.id!!)
        }
        reviewRepository.deleteReviewById(id)
    }
}