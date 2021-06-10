package edu.kit.pse.a1sicht.networking

import edu.kit.pse.a1sicht.database.entities.Administrator
import io.reactivex.Observable
import retrofit2.http.*

/**
 *  This interface serves for making REST calls to the server with the /admin endpoints.
 *  @author Stanimir Bozhilov, Martin Zahariev
 */
interface AdministratorService {

    /**
     * This method creates a new Administrator object and sends it to the server
     * @param [admin] The newly created Administrator
     */
    @POST("/admins/")
    fun create(@Body admin: Administrator): Observable<Administrator>

    /**
     * This method gets all Administrators from the server.
     * @return [List<Administrator>] containing all Administrators in the server
     */
    @GET("/admins/")
    fun getAll(): Observable<List<Administrator>>

    /**
     * This methods gets an Administrator with the given ID number from the server.
     * @param [adminId] The ID number of the administrator.
     * @return [Administrator]
     */
    @GET("/admins/{adminId}")
    fun getById(@Path("adminId") adminId: Int): Observable<Administrator>

    /**
     * This method updates an Administrator on the server
     * @param [adminId] The ID number of the Administrator to be updated
     * @param [admin] An Administrator object holding the new data
     * @return [Administrator] The updated Administrator
     */
    @PUT("/admins/{adminId}")
    fun updateById(@Path("adminId") adminId: Int, @Body admin: Administrator): Observable<Administrator>

    /**
     * This method deletes Administrator from the server
     * @param [adminId] The ID number of the Administrator to be deleted
     * @return The deleted Administrator
     */
    @DELETE("/admins/{adminId}")
    fun deleteById(@Path("adminId") adminId: Int): Observable<Administrator>

}