package edu.kit.pse.a1sicht.networking

import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Review
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 *  This interface serves for making REST calls to the server with the /employee endpoints.
 *  @author Stanimir Bozhilov, Martin Zahariev
 */
interface EmployeeService {

    /**
     * This method gets all Employees from the server.
     * @return [List<Employee>] containing all Employees in the server
     */
    @GET("/employees/")
    fun getAll(): Observable<List<Employee>>

    /**
     * This methods gets an Employee with the given ID number from the server.
     * @param [employeeId] The ID number of the administrator.
     * @return [Employee]
     */
    @GET("/employees/{employeeId}")
    fun getById(@Path("employeeId") employeeId: Int): Observable<Employee>

    /**
     * This method updates an Employee on the server
     * @param [employeeId] The ID number of the Employee to be updated
     * @param [employee] An Employee object holding the new data
     * @return [Employee] The updated Employee
     */
    @PUT("/employees/{employeeId}")
    fun updateById(@Path("employeeId") employeeId: Int, @Body employee: Employee): Observable<Employee>

    /**
     * This method deletes Employee from the server
     * @param [employeeId] The ID number of the Employee to be deleted
     * @return The deleted Employee
     */
    @DELETE("/employees/{employeeId}")
    fun deleteById(@Path("employeeId") employeeId: Int): Observable<Employee>

    /**
     * This method verifies an Employee with the given ID number
     * @param [employeeId] The ID number of the Employee
     * @return true if the Employee was verified successfully, false otherwise
     */
    @PUT("/employees/verify/{employeeId}")
    fun verifyById(@Path("employeeId") employeeId: Int): Call<Void>

    /**
     * This method gets all reviews for an Employee
     * @param [employeeId] The ID number of the Employee
     * @return [List<Review>] containing all Reviews for this Employee
     */
    @GET("/employees/{employeeId}/reviews")
    fun getReviews(@Path("employeeId") employeeId: Int): Observable<List<Review>>

    /**
     * This method creates a new Employee object and sends it to the server
     * @param [employee] The newly created Employee
     */
    @POST("/employees/create")
    fun createEmployee(@Body employee: Employee): Observable<Employee>

    /**
     * This method gets a Review with the given ID number
     * @param [employeeId] The ID number of the Employee
     * @param [reviewId] The ID number of the Review
     * @return The Review object with the given ID number
     */
    @GET("/employees/{employeeId}/reviews/{reviewId}")
    fun getReviewById(@Path("employeeId") employeeId: Int, @Path("reviewId") reviewId: Int): Observable<Review>

    /**
     * This method gets the creator of a review
     * @param [reviewId] The ID number of the review
     * @return [Employee] which has created this review
     */
    @GET("/employees/{reviewId}")
    fun getEmployeeByReview(@Path("reviewId") reviewId: Int): Observable<Employee>

}