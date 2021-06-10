package edu.kit.pse.a1sicht.networking

import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import edu.kit.pse.a1sicht.database.entities.Employee
import edu.kit.pse.a1sicht.database.entities.Review
import okhttp3.OkHttpClient
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.sql.Timestamp

/**
 *
 * @author Martin Zahariev
 */
class ReviewServiceTest {
    @Rule
    private var mockWebServer = MockWebServer()
    private lateinit var reviewService: ReviewService

    @Before
    fun setUp(){
        mockWebServer.start()

        reviewService = Retrofit.Builder()
            .baseUrl("/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(ReviewService::class.java)

        reviewService.create(
            Review(1, "TGI", "SR237",
                Timestamp(2019, 9, 10, 15, 0, 0,0),
                20, 3, 1, "Theoretische Grundlagen der Informatik Klausureinsicht"))
    }

    @Test
    fun testCreate(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_CREATED)
            .setBody("{" +
                "\" reviewID \" : 2," +
                "\" reviewName \" : \"LA I Einsicht \"," +
                "\" reviewRoom \" : \" Audimax \"," +
                "\" dateOfReview \" :" +
                "{" +
                    "\" day\" : 12," +
                    "\" month \" : 9," +
                    "\" year \" : 2018," +
                    "\" hour \" : 12," +
                    "\" minute \" : 30" +
                    "}," +
                "\" creator \" : 23333," +
                "\" currentCountOfStudents \" : 110" +
                "}")

        mockWebServer.enqueue(response)

        //Act
        val result = reviewService.create(Review(2, "LA I Einsicht", "Audimax",
            Timestamp(2018, 9, 12, 12, 30, 0,0),
            20, 3, 23333, "LA I Einsicht")).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertEquals(2, result.id)
        Assert.assertEquals("LA I Einsicht", result.name)
        Assert.assertEquals("Audimax", result.room)
        Assert.assertEquals(23333,result.employee_id)
        Assert.assertEquals(3,result.numberOfTimeSlots)

    }

    @Test
    fun testGetAll(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_CREATED)
            .setBody("{" +
                    "\" reviewID \" : 1," +
                    "\" reviewName \" : \"TGI \"," +
                    "\" reviewRoom \" : \" SR237 \"," +
                    "\" dateOfReview \" :" +
                    "{" +
                    "\" day\" : 10," +
                    "\" month \" : 9," +
                    "\" year \" : 2019," +
                    "\" hour \" : 15," +
                    "\" minute \" : 0" +
                    "}," +
                    "\" creator \" : 1," +
                    "\" currentCountOfStudents \" : 110" +
                    "}")

        mockWebServer.enqueue(response)

        //Act
        val result = reviewService.getAll().blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.get(1).id)
        Assert.assertEquals("TGI", result.get(1).name)
        Assert.assertEquals("SR237", result.get(1).room)
        Assert.assertEquals(1,result.get(1).employee_id)
        Assert.assertEquals(3,result.get(1).numberOfTimeSlots)
    }

    @Test
    fun testGetById(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" reviewID \" : 1," +
                    "\" reviewName \" : \"TGI \"," +
                    "\" reviewRoom \" : \" SR237 \"," +
                    "\" dateOfReview \" :" +
                    "{" +
                    "\" day\" : 10," +
                    "\" month \" : 9," +
                    "\" year \" : 2019," +
                    "\" hour \" : 15," +
                    "\" minute \" : 0" +
                    "}," +
                    "\" creator \" : 1," +
                    "\" currentCountOfStudents \" : 110" +
                    "}")


        mockWebServer.enqueue(response)

        //Act
        val result = reviewService.getById(1).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.id)
        Assert.assertEquals("TGI", result.name)
        Assert.assertEquals("SR237", result.room)
        Assert.assertEquals(1,result.employee_id)
        Assert.assertEquals(3,result.numberOfTimeSlots)
    }
/*
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
            Employee(2, "tsesok", "Tsvetan", "Sokolov", true)
        ).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertTrue(result.googleID.equals("brurez"))
        Assert.assertTrue(result.firstName.equals("Tsvetan"))
        Assert.assertTrue(result.lastName.equals("Sokolov"))
        Assert.assertTrue(result.verified)

    }
*/
    @Test
    fun testDeleteById(){
        //Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{" +
                    "\" reviewID \" : 1," +
                    "\" reviewName \" : \"TGI \"," +
                    "\" reviewRoom \" : \" SR237 \"," +
                    "\" dateOfReview \" :" +
                    "{" +
                    "\" day\" : 10," +
                    "\" month \" : 9," +
                    "\" year \" : 2019," +
                    "\" hour \" : 15," +
                    "\" minute \" : 0" +
                    "}," +
                    "\" creator \" : 1," +
                    "\" currentCountOfStudents \" : 110" +
                    "}")


        mockWebServer.enqueue(response)

        //Act
        val result = reviewService.deleteById(1).blockingFirst()

        //Assert
        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.id)
        Assert.assertEquals("TGI", result.name)
        Assert.assertEquals("SR237", result.room)
        Assert.assertEquals(1,result.employee_id)
        Assert.assertEquals(3,result.numberOfTimeSlots)

    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}