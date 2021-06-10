package controller

import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.config.HoconApplicationConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.formUrlEncode
import io.ktor.server.testing.*
import junit.framework.Assert.assertNotNull
import module
import org.joda.time.DateTime
import org.joda.time.DateTime.parse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.ReviewRepository
import repository.TimeslotRepository
import java.sql.Date
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class ReviewControllerTest {

    protected lateinit var engine: TestApplicationEngine

    private var reviewRepository: ReviewRepository = ReviewRepository()
    private var timeslotRepository: TimeslotRepository = TimeslotRepository()
    private var id: Int? = null
    private var timeslotId: Int? = null

    @BeforeEach
    fun setUpTestServer() {
        engine = TestEngine.create(createTestEnvironment {
            config = HoconApplicationConfig(ConfigFactory.load("jwt.conf"))
        }){}
        engine.start()
        engine.application.module()
    }

    @Test
    fun testCreate(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Post, "/reviews/create"){
            var date = DateTime.now()
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "name":"hm", "location":"SR -101", "date":{"datetime":"2019-08-31T19:36:08.855+02:00"}, "lengthOfTimeslot":20, 
                "numberOfTimeslots":5, "createdBy":12, "description":"blabla"}""")
        }.apply {
            assertNotNull(response.content)
            assertEquals(201, response.status()?.value)
        }
    }

    @Test
    fun testGet(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Get, "/reviews/").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGetById(): Unit = withTestApplication(Application::module) {
        id = reviewRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/reviews/${id}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testUpdateById(): Unit = withTestApplication(Application::module) {
        id = reviewRepository.getAll()[0].id
        handleRequest(HttpMethod.Put, "/reviews/${id}"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "name":"hm", "location":"SR -101", "date":{"datetime":"2019-09-15T19:36:08.855+02:00"}, "lengthOfTimeslot":20, 
                "numberOfTimeslots":5, "createdBy":12, "description":"blabla"}""")
        }.apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testDeleteById(): Unit = withTestApplication(Application::module) {
        id = reviewRepository.getAll()[0].id
        handleRequest(HttpMethod.Delete, "/reviews/${id}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGetTimeslots(): Unit = withTestApplication(Application::module) {
        id = reviewRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/reviews/${id}/timeslots").apply {
            assertEquals(404, response.status()?.value)
            assertNull(response.content)
        }
    }

    @Test
    fun testCreateTimeslot(): Unit = withTestApplication(Application::module) {
        id = reviewRepository.getAll()[0].id
        handleRequest(HttpMethod.Post, "/reviews/${id}/timeslots"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "startTime":{"datetime":"2019-09-15T19:36:08.855+02:00"}, "duration":20,
                "maxNumberOfStudents":15, "currentNumberOfStudents":0, "belongsTo":1}""")
        }.apply {
            assertEquals(201, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGetTimeslot(): Unit = withTestApplication(Application::module) {
        id = reviewRepository.getAll()[0].id
        timeslotId = timeslotRepository.getAllForReview(id!!.toInt())[0].id
        handleRequest(HttpMethod.Get, "/reviews/${id}/timeslots/${timeslotId}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testUpdateTimeslot(): Unit = withTestApplication(Application::module) {
        id = reviewRepository.getAll()[0].id
        //timeslotId = timeslotRepository.getAllForReview(id!!.toInt())[0].id
        handleRequest(HttpMethod.Put, "/reviews/${id}/timeslots/${1}"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "startTime":{"datetime":"2019-09-25T19:36:08.855+02:00"}, "duration":20,
                "maxNumberOfStudents":12, "currentNumberOfStudents":0, "belongsTo":1}""")
        }.apply {
            assertEquals(404, response.status()?.value)
            assertNull(response.content)
        }
    }

    @Test
    fun testDeleteTimeslot(): Unit = withTestApplication(Application::module) {
        id = reviewRepository.getAll()[0].id
        handleRequest(HttpMethod.Delete, "/reviews/${id}/timeslots/${1}").apply {
            assertEquals(404, response.status()?.value)
            assertNull(response.content)
        }
    }

    @Test
    fun testSignUp(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Post, "/signUp/${1}/${1}"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"studentId":1, "timeslotId":1}""")
        }.apply {
            assertNotEquals(403, response.status()?.value)
            assertNull(response.content)
        }
    }

    @Test
    fun testSignOut(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Post, "/signOut/${1}/${1}"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"studentId":1, "timeslotId":1}""")
        }.apply {
            assertNotEquals(200, response.status()?.value)
            assertNull(response.content)
        }
    }

    @Test
    fun testChangeTimeslot(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Put, "/changeTimeslot/${1}/${2}/${1}"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"studentId":1, "timeslotId":2}""")
        }.apply {
            assertNotEquals(200, response.status()?.value)
            assertNull(response.content)
        }
    }

    @Test
    fun testGetStudents(): Unit = withTestApplication(Application::module) {
        id = reviewRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/reviews/${id}/students").apply {
            assertEquals(404, response.status()?.value)
            assertNull(response.content)
        }
    }

    @AfterEach
    fun stopServer(){
        engine.stop(0L, 0L, TimeUnit.MILLISECONDS)
    }

}