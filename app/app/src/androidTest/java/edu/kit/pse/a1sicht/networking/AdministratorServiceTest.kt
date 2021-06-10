package edu.kit.pse.a1sicht.networking

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import edu.kit.pse.a1sicht.database.entities.Administrator
import okhttp3.OkHttpClient
import org.junit.*
import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.net.HttpURLConnection

/**
 * @author Martin Zahariev
 */
class AdministratorServiceTest {

    @Rule
    private var mockWebServer = MockWebServer()
    private lateinit var administratorService: AdministratorService

    @Before
    fun setUp(){
        mockWebServer.start()
        administratorService = Retrofit.Builder()
            .baseUrl("/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(AdministratorService::class.java)
        administratorService.create(Administrator(1, "rogfed", "Roger", "Federer"))
    }

    @Test
    fun testCreate(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_CREATED)
            .setBody("{" +
                    "\" adminId \" : 2," +
                    "\" googleId \" : \" rafnad \"," +
                    "\" firstName \" : \" Rafael \"," +
                    "\" lastName \" : \" Nadal \"" +
                    "}")


        mockWebServer.enqueue(response)
        //Act
        val result = administratorService.create(
            Administrator(2, "rafnad", "Rafael", "Nadal")
        ).blockingFirst()

        assertNotNull(response)
        assertTrue(result.googleID.equals("rafnad"))
        assertTrue(result.firstName.equals("Rafael"))
        assertTrue(result.lastName.equals("Nadal"))
    }

    @Test
    fun testGetAll(){
        //Assign
        val successResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" adminId \" : 1," +
                    "\" googleId \" : \" rogfed \"," +
                    "\" firstName \" : \" Roger \"," +
                    "\" lastName \" : \" Federer \"" +
                    "}")

        mockWebServer.enqueue(successResponse)

        //Act
        val result = administratorService.getAll().blockingFirst()

        //Assert
        assertNotNull(result)
        assertEquals(1,result.size)
        assertTrue(result.get(1).googleID.equals("rogfed"))
        assertTrue(result.get(1).firstName.equals("Roger"))
        assertTrue(result.get(1).lastName.equals("Federer"))

    }

    @Test
    fun testGetById(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" adminId \" : 1," +
                    "\" googleId \" : \" rogfed \"," +
                    "\" firstName \" : \" Roger \"," +
                    "\" lastName \" : \" Federer \"" +
                    "}")


        mockWebServer.enqueue(response)

        //Act

        val result = administratorService.getById(1).blockingFirst()

        //Assert
        assertNotNull(result)
        assertTrue(result.googleID.equals("rogfed"))
        assertTrue(result.firstName.equals("Roger"))
        assertTrue(result.lastName.equals("Federer"))
    }

    @Test
    fun testUpdateById(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" adminId \" : 1," +
                    "\" googleId \" : \" novdjo \"," +
                    "\" firstName \" : \" Novak \"," +
                    "\" lastName \" : \" Djokovic \"" +
                    "}")


        mockWebServer.enqueue(response)

        //Act
        val result = administratorService
            .updateById(1, Administrator(3, "novdjo", "Novak", "Djokovic")).blockingFirst()

        //Assert
        assertNotNull(result)
        assertTrue(result.googleID.equals("novdjo"))
        assertTrue(result.firstName.equals("Novak"))
        assertTrue(result.lastName.equals("Djokovic"))

    }

    @Test
    fun testDeleteById(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" adminId \" : 1," +
                    "\" googleId \" : \" rogfed \"," +
                    "\" firstName \" : \" Roger \"," +
                    "\" lastName \" : \" Federer \"" +
                    "}")


        mockWebServer.enqueue(response)

        //Act
        val result = administratorService.deleteById(1).blockingFirst()

        //Assert
        assertNotNull(result)
        assertTrue(result.googleID.equals("rogfed"))
        assertTrue(result.firstName.equals("Roger"))
        assertTrue(result.lastName.equals("Federer"))
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}