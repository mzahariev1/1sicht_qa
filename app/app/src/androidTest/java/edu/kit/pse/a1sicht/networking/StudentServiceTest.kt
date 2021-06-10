package edu.kit.pse.a1sicht.networking

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import edu.kit.pse.a1sicht.database.entities.Student
import okhttp3.OkHttpClient
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

/**
 *
 * @author Martin Zahariev
 */
class StudentServiceTest {

    @Rule
    private var mockWebServer = MockWebServer()
    private lateinit var studentService: StudentService

    @Before
    fun setUp(){
        mockWebServer.start()

        studentService = Retrofit.Builder()
            .baseUrl("/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(StudentService::class.java)

        studentService.create(Student(1, "lebjam", "LeBron", "James", 23))
    }

    @Test
    fun testCreate(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_CREATED)
            .setBody("{" +
                "\" studentId \" : 2," +
                "\" googleId \" : \" lukdon \"," +
                "\" firstName \" : \" Luka \"," +
                "\" lastName \" : \" Doncic \"," +
                "\" matriculationNumber \" : 77," +
                "\" reviews \": [" +
                "\" reviewID \" : 1234," +
                "\" reviewID \" : 5678" +
                "]" +
                "}")


        mockWebServer.enqueue(response)
        //Act
        val result = studentService.create(
            Student(2, "lukdon", "Luka", "Doncic", 77)
        ).blockingFirst()

        Assert.assertNotNull(response)
        Assert.assertTrue(result.googleID.equals("lukdon"))
        Assert.assertTrue(result.firstName.equals("Luka"))
        Assert.assertTrue(result.lastName.equals("Doncic"))
        Assert.assertEquals(77, result.matriculationNumber)
    }

    @Test
    fun testGetAll(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_CREATED)
            .setBody("{" +
                    "\" studentId \" : 1," +
                    "\" googleId \" : \" lebjam \"," +
                    "\" firstName \" : \" LeBron \"," +
                    "\" lastName \" : \" James \"," +
                    "\" matriculationNumber \" : 23," +
                    "\" reviews \": [" +
                    "\" reviewID \" : 1234," +
                    "\" reviewID \" : 5678" +
                    "]" +
                    "}")

        mockWebServer.enqueue(response)

        //Act
        val result = studentService.getAll().blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.size)
        Assert.assertTrue(result.get(1).googleID.equals("lebjam"))
        Assert.assertTrue(result.get(1).firstName.equals("LeBron"))
        Assert.assertTrue(result.get(1).lastName.equals("James"))
        Assert.assertEquals(23, result.get(1).matriculationNumber)
    }

    @Test
    fun testGetById(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" studentId \" : 1," +
                    "\" googleId \" : \" lebjam \"," +
                    "\" firstName \" : \" LeBron \"," +
                    "\" lastName \" : \" James \"," +
                    "\" matriculationNumber \" : 23," +
                    "\" reviews \": [" +
                    "\" reviewID \" : 1234," +
                    "\" reviewID \" : 5678" +
                    "]" +
                    "}")


        mockWebServer.enqueue(response)

        //Act
        val result = studentService.getById(1).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertTrue(result.googleID.equals("lebjam"))
        Assert.assertTrue(result.firstName.equals("LeBron"))
        Assert.assertTrue(result.lastName.equals("James"))
        Assert.assertEquals(23, result.matriculationNumber)
    }

    @Test
    fun testUpdateById(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" studentId \" : 1," +
                    "\" googleId \" : \" lebjam \"," +
                    "\" firstName \" : \" LeBron \"," +
                    "\" lastName \" : \" James \"," +
                    "\" matriculationNumber \" : 23," +
                    "\" reviews \": [" +
                    "\" reviewID \" : 1234," +
                    "\" reviewID \" : 5678" +
                    "]" +
                    "}")


        mockWebServer.enqueue(response)

        //Act
        val result = studentService.updateById(1,
            Student(2, "jamhar", "James", "Harden",13)
        ).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertTrue(result.googleID.equals("jamhar"))
        Assert.assertTrue(result.firstName.equals("James"))
        Assert.assertTrue(result.lastName.equals("Harden"))
        Assert.assertEquals(13, result.matriculationNumber)

    }

    @Test
    fun testDeleteById(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" studentId \" : 1," +
                    "\" googleId \" : \" lebjam \"," +
                    "\" firstName \" : \" LeBron \"," +
                    "\" lastName \" : \" James \"," +
                    "\" matriculationNumber \" : 23," +
                    "\" reviews \": [" +
                    "\" reviewID \" : 1234," +
                    "\" reviewID \" : 5678" +
                    "]" +
                    "}")


        mockWebServer.enqueue(response)

        //Act
        val result = studentService.deleteById(1).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertTrue(result.googleID.equals("lebjam"))
        Assert.assertTrue(result.firstName.equals("LeBron"))
        Assert.assertTrue(result.lastName.equals("James"))
        Assert.assertEquals(23, result.matriculationNumber)

    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}