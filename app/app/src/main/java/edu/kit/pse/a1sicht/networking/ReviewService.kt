package edu.kit.pse.a1sicht.networking

import edu.kit.pse.a1sicht.database.entities.Review
import edu.kit.pse.a1sicht.database.entities.Timeslot
import retrofit2.http.*
import io.reactivex.Observable
import retrofit2.Call

/**
 *  This interface serves for making REST calls to the server with the /review endpoints.
 *  @author Stanimir Bozhilov, Martin Zahariev
 */
interface ReviewService {

    /**
     * This method creates a new Review object and sends it to the server
     * @param [review] The newly created Review
     */
    @POST("/reviews/create")
    fun create(@Body review: Review): Observable<Review>

    /**
     * This method gets all Reviews from the server.
     * @return [List<Review>] containing all Reviews in the server
     */
    @GET("/reviews/")
    fun getAll(): Observable<List<Review>>

    /**
     * This methods gets a Иеэсеу with the given ID number from the server.
     * @param [reviewId] The ID number of the student.
     * @return [Review]
     */
    @GET("/reviews/{reviewId}")
    fun getById(@Path("reviewId") reviewId: Int): Observable<Review>

    /**
     * This method gets all Reviews which match a given set of keywords.
     * @param [keywords] A String object containing all keywords for the search
     * @return [List<Review>] containing all Reviews which match the given keywords
     */
    @GET("/reviews/")
    fun getByKeywords(@Query("keywords") keywords: String): Observable<List<Review>>

    /**
     * This method updates a Review on the server
     * @param [reviewId] The ID number of the Review to be updated
     * @param [review] A Review object holding the new data
     * @return [Review] The updated Review
     */
    @PUT("/reviews/{reviewId}")
    fun updateById(@Path("reviewId") reviewId: Int, @Body review: Review): Observable<Review>

    /**
     * This method deletes Review from the server
     * @param [reviewId] The ID number of the Review to be deleted
     * @return [Review] The deleted Review object
     */
    @DELETE("/reviews/{reviewId}")
    fun deleteById(@Path("reviewId") reviewId: Int): Observable<Review>

    /**
     * This method gets all Timeslots for a Review with the given ID number.
     * @param [reviewId] The ID number of the Review
     * @return [List<Timeslot>] containg all Timeslots for the given Review
     */
    @GET("/reviews/{reviewId}/timeslots")
    fun getAllTimeslots(@Path("reviewId") reviewId: Int): Observable<List<Timeslot>>

    /**
     * This method creates a new Timeslot and adds it to a Review with the given ID number
     * @param [reviewId] The ID number of the Review
     * @param [timeslot] The newly created Timeslot
     */
    @POST("/reviews/{reviewId}/timeslots")
    fun createTimeslot(@Path("reviewId") reviewId: Int, @Body timeslot: Timeslot): Observable<Timeslot>

    /**
     * This method gets a Timeslot by a given ID number
     * @param [reviewId] The ID number of the Review
     * @param [timeslotId] The ID number of the Timeslot
     * @return [Timeslot]
     */
    @GET("/reviews/{reviewId}/timeslots/{timeslotId}")
    fun getTimeslotById(@Path("reviewId") reviewId: Int, @Path("timeslotId") timeslotId: Int): Observable<Timeslot>

    /**
     * This method updates a Timeslot by a given ID number
     * @param [reviewId] The ID number of the Review
     * @param [timeslotId] The ID number of the Timeslot
     * @param [timeslot] A Timeslot object holding the data for the update
     * @return [Timeslot] The updated Timeslot object
     * @return [Timeslot] The updated Timeslot object
     */
    @PUT("/reviews/{reviewId}/timeslots/{timeslotId}")
    fun updateTimeslotById(@Path("reviewId") reviewId: Int, @Path("timeslotId") timeslotId: Int, @Body timeslot: Timeslot): Observable<Timeslot>

    /**
     * This method deletes a Timeslot with a given ID number
     * @param [reviewId] The ID number of the Review
     * @param [timeslotId] The ID number of the Timeslot
     * @return [Timeslot] The deleted Timeslot object
     */
    @DELETE("/reviews/{reviewId}/timeslots/{timeslotId}")
    fun deleteTimeslotById(@Path("reviewId") reviewId: Int, @Path("timeslotId") timeslotId: Int): Observable<Timeslot>

    /**
     * This method signs up a student for a Timeslot in a Review
     * @param [timeslotId] The ID number of the Timeslot
     * @param [studentId] The ID number of the Student
     * @return [true] if the student has signed up successfully, false otherwise
     */
    @POST("reviews/signUp/{timeslotId}/{studentId}")
    fun signUpForTimeslot(@Path("timeslotId") timeslotId: Int, @Path("studentId") studentId: Int): Call<Void>

    /**
     * This method signs out a student from a Timeslot in a Review
     * @param [timeslotId] The ID number of the Timeslot
     * @param [studentId] The ID number of the Student
     * @return [true] if the student has signed out successfully, false otherwise
     */
    @POST("reviews/signOut/{timeslotId}/{studentId}")
    fun signOutFromTimeslot(@Path("timeslotId") timeslotId: Int, @Path("studentId") studentId: Int): Call<Void>

    /**
     * This method changes the Timeslot for which a Student has signed up in a given Review
     * @param [reviewId] The ID number of the Review
     * @param [oldTimeslotId] The ID number of the old Timeslot
     * @param [newTimeslotId] The ID number of the new Timeslot
     * @param [studentId] The ID number of the Student
     * @return [Timeslot] The new Timeslot for which the Student has signed up
     */
    @PUT("reviews/changeTimeslot/{oldTimeslotId}/{newTimeslotId}/{studentId}")
    fun changeTimeslotOfStudent(
        @Path("oldTimeslotId") oldTimeslotId: Int, @Path("newTimeslotId") newTimeslotId: Int, @Path(
            "studentId"
        ) studentId: Int
    ): Observable<Timeslot>

}