package edu.kit.pse.a1sicht.networking

import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.database.entities.Student
import io.reactivex.Observable
import edu.kit.pse.a1sicht.database.entities.Timeslot
import retrofit2.http.*

/**
 *  This interface serves for making REST calls to the server with the /student endpoints.
 *  @author Stanimir Bozhilov, Martin Zahariev
 */
interface StudentService {

    /**
     * This method creates a new Student object and sends it to the server
     * @param [student] The newly created Student
     */
    @POST("/students/create")
    fun create(@Body student: Student): Observable<Student>

    /**
     * This method gets all Students from the server.
     * @return [List<Student>] containing all Students in the server
     */
    @GET("/students/")
    fun getAll(): Observable<List<Student>>

    /**
     * This methods gets a Student with the given ID number from the server.
     * @param [studentId] The ID number of the student.
     * @return [Student]
     */
    @GET("/students/{studentId}")
    fun getById(@Path("studentId") studentId: Int): Observable<Student>

    /**
     * This method updates a Student on the server
     * @param [studentId] The ID number of the Student to be updated
     * @param [student] A Student object holding the new data
     * @return [Student] The updated Student
     */
    @PUT("/students/{studentId}")
    fun updateById(@Path("studentId") studentId: Int, @Body student: Student): Observable<Student>

    /**
     * This method deletes Student from the server
     * @param [studentId] The ID number of the Student to be deleted
     * @return The deleted Student
     */
    @DELETE("/students/{studentId}")
    fun deleteById(@Path("studentId") studentId: Int): Observable<Student>

    /**
     * This method gets all Reviews for which the Student has registered
     * @param [studentId] The ID number of the student
     * @return [List<Review>] containing all reviews for this student
     */
    @GET("/students/{studentId}/reviews")
    fun getReviews(@Path("studentId") studentId: Int): Observable<List<Review>>

    /**
     * This method gets all Timeslots for which the Student has registered
     * @param [studentId] The ID number of the student
     * @return [List<Timeslots>] containing all timeslots for this student
     */
    @GET("/students/{studentId}/timeslots")
    fun getTimeslots(@Path("studentId") studentId: Int): Observable<List<Timeslot>>

    /**
     * This method gets the timeslot for which a student has signed up in a given review
     * @param [studentId] The ID number of the student
     * @param [reviewId] The ID number of the review
     * @return [Timeslot] for which the student has signed up in the given review
     */
    @GET("/students/{studentId}/{reviewId}")
    fun getTimeslotInReview(@Path("studentId") studentId: Int, @Path("reviewId") reviewId: Int): Observable<Timeslot>

    /**
     * This method gets all students which have signed up for a given review
     * @param [reviewId] The unique ID number of the review
     * @return [List<Student>] containing all students which have signed up for the review
     */
    @GET("/students/{reviewId}")
    fun getForReview(@Path("reviewId") reviewId: Int): Observable<List<Student>>

    /**
     * This method gets all students which have signed up for a given timeslot
     * @param [timeslotIdId] The unique ID number of the timeslot
     * @return [List<Student>] containing all students which have signed up for the timeslot
     */
    @GET("/students/{timeslotId}")
    fun getForTimeslot(@Path("timeslotId") timeslotId: Int): Observable<List<Student>>

}