package edu.kit.pse.a1sicht.networking

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import edu.kit.pse.a1sicht.database.entities.Employee
import okhttp3.OkHttpClient
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

/**
 *
 * @author Martin Zahariev
 */
class EmployeeServiceTest {

    @Rule
    private var mockWebServer = MockWebServer()
    private lateinit var employeeService: EmployeeService

    @Before
    fun setUp(){
        mockWebServer.start()

        employeeService = Retrofit.Builder()
                .baseUrl("/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .build()
                .create(EmployeeService::class.java)

        employeeService.createEmployee(Employee(1, "brurez", "Bruno", "Rezende", true))
    }

    @Test
    fun testGetAll(){
        //Assign
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_CREATED)
                .setBody("{" +
                        "\" employeeId \" : 1," +
                        "\" googleId \" : \" brurez \"," +
                        "\" firstName \" : \" Bruno \"," +
                        "\" lastName \" : \" Rezende \"," +
                        "\" isVerified \" : true\"" +
                        "}")

        mockWebServer.enqueue(response)

        //Act
        val result = employeeService.getAll().blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.size)
        Assert.assertTrue(result.get(1).googleID.equals("brurez"))
        Assert.assertTrue(result.get(1).firstName.equals("Bruno"))
        Assert.assertTrue(result.get(1).lastName.equals("Rezende"))
        Assert.assertTrue(result.get(1).verified)
    }

    @Test
    fun testGetById(){
        //Assign
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("{" +
                        "\" employeeId \" : 1," +
                        "\" googleId \" : \" brurez \"," +
                        "\" firstName \" : \" Bruno \"," +
                        "\" lastName \" : \" Rezende \"," +
                        "\" isVerified \" : true\"" +
                        "}")


        mockWebServer.enqueue(response)

        //Act
        val result = employeeService.getById(1).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertTrue(result.googleID.equals("brurez"))
        Assert.assertTrue(result.firstName.equals("Bruno"))
        Assert.assertTrue(result.lastName.equals("Rezende"))
        Assert.assertTrue(result.verified)
    }

    @Test
    fun testUpdateById(){
        //Assign
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("{" +
                        "\" employeeId \" : 1," +
                        "\" googleId \" : \" brurez \"," +
                        "\" firstName \" : \" Tsvetan \"," +
                        "\" lastName \" : \" Sokolov \"," +
                        "\" isVerified \" : true\"" +
                        "}")


        mockWebServer.enqueue(response)

        //Act
        val result = employeeService.updateById(1,
            Employee(2, "tsesok", "Tsvetan", "Sokolov", true)).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertTrue(result.googleID.equals("brurez"))
        Assert.assertTrue(result.firstName.equals("Tsvetan"))
        Assert.assertTrue(result.lastName.equals("Sokolov"))
        Assert.assertTrue(result.verified)

    }

    @Test
    fun testDeleteById(){
        //Assign
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("{" +
                        "\" employeeId \" : 1," +
                        "\" googleId \" : \" brurez \"," +
                        "\" firstName \" : \" Bruno \"," +
                        "\" lastName \" : \" Rezende \"," +
                        "\" isVerified \" : true\"" +
                        "}")


        mockWebServer.enqueue(response)

        //Act
        val result = employeeService.deleteById(1).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertTrue(result.googleID.equals("brurez"))
        Assert.assertTrue(result.firstName.equals("Bruno"))
        Assert.assertTrue(result.lastName.equals("Rezende"))
        Assert.assertTrue(result.verified)

    }

    @Test
    fun testVerifyById(){
        //Assign
        val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody("{" +
                        "\" employeeId \" : 2," +
                        "\" googleId \" : \" tsesok \"," +
                        "\" firstName \" : \" Tsvetan \"," +
                        "\" lastName \" : \" Sokolov \"," +
                        "\" isVerified \" : true\"" +
                        "}")


        mockWebServer.enqueue(response)

        //Act
        employeeService.createEmployee(Employee(2, "tsesok", "Tsvetan", "Sokolov", false))
        employeeService.verifyById(2)

        //Assert
        Assert.assertTrue(employeeService.getById(2).blockingFirst().verified)

    }

    @Test
    fun testCreate(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" employeeId \" : 2," +
                    "\" googleId \" : \" tsesok \"," +
                    "\" firstName \" : \" Tsvetan \"," +
                    "\" lastName \" : \" Sokolov \"," +
                    "\" isVerified \" : true\"" +
                    "}")

        mockWebServer.enqueue(response)

        //Act
        val result = employeeService.createEmployee(
            Employee(2, "tsesok", "Tsvetan", "Sokolov", true)
        ).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertEquals(2, result.id)
        Assert.assertTrue(result.googleID.equals("tsesok"))
        Assert.assertTrue(result.firstName.equals("Tsvetan"))
        Assert.assertTrue(result.lastName.equals("Sokolov"))
        Assert.assertTrue(result.verified)

    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}