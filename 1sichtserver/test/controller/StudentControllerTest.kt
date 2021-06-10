package controller

import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.config.HoconApplicationConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.formUrlEncode
import io.ktor.server.testing.*
import junit.framework.Assert
import module
import org.junit.Assert.assertNotNull
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.ReviewRepository
import repository.StudentRepository
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class StudentControllerTest {

    protected lateinit var engine: TestApplicationEngine

    private var studentRepository: StudentRepository = StudentRepository()
    private var reviewRepository:ReviewRepository = ReviewRepository()
    private var id: Int? = null
    private var reviewId: Int? = null

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
        handleRequest(HttpMethod.Post, "/students/create"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "googleId":"123", "firstName":"Gosho", "lastName":"Petrov", "matriculationNumber":66466}""")
        }.apply {
            assertEquals(201, response.status()?.value)
            Assert.assertNotNull(response.content)
        }
    }

    @Test
    fun testGet(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Get, "/students/").apply {
            assertEquals(200, response.status()?.value)
            Assert.assertNotNull(response.content)
        }
    }

    @Test
    fun testGetById(): Unit = withTestApplication(Application::module) {
        id = studentRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/students/${id}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testUpdateById(): Unit = withTestApplication(Application::module) {
        id = studentRepository.getAll()[0].id
        handleRequest(HttpMethod.Put, "/students/${id}"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "googleId":"123", "firstName":"Gosho", "lastName":"Petrov", "matriculationNumber":55555}""")
        }.apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testDeleteById(): Unit = withTestApplication(Application::module) {
        id = studentRepository.getAll()[0].id
        handleRequest(HttpMethod.Delete, "/students/${id}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGetReviews(): Unit = withTestApplication(Application::module) {
        //id = studentRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/students/${1}/reviews").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGetTimeslots(): Unit = withTestApplication(Application::module) {
        //id = studentRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/students/${1}/timeslots").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGetByReviewId(): Unit = withTestApplication(Application::module) {
        reviewId = reviewRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/students/${reviewId}").apply {
            assertEquals(404, response.status()?.value)
            assertNull(response.content)
        }
    }

    @Test
    fun testGetByTimeslotId(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Get, "/students/${1}").apply {
            assertEquals(404, response.status()?.value)
            assertNull(response.content)
        }
    }

    @Test
    fun testGetTimeslotOfStudentForReview(): Unit = withTestApplication(Application::module) {
        //id = studentRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/students/${1}/${1}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @AfterEach
    fun stopServer(){
        engine.stop(0L, 0L, TimeUnit.MILLISECONDS)
    }

}