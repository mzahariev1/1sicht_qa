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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.EmployeeRepository
import repository.ReviewRepository
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EmployeeControllerTest {

    protected lateinit var engine: TestApplicationEngine

    private var employeeRepository: EmployeeRepository = EmployeeRepository()
    private var reviewRepository: ReviewRepository = ReviewRepository()
    private var id: Int? = null
    private var reviewId: Int? = 0

    @BeforeEach
    fun setUpTestServer(){
        engine = TestEngine.create(createTestEnvironment {
            config = HoconApplicationConfig(ConfigFactory.load("jwt.conf"))
        }){}
        engine.start()
        engine.application.module()

    }

    @Test
    fun testCreate(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Post, "/employees/create") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "googleId":"123", "firstName":"Gosho", "lastName":"Petrov", "verified":false}""")
        }.apply {
            assertNotNull(response.content)
            assertEquals(201, response.status()?.value)
        }
    }

    @Test
    fun testGet(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Get, "/employees/").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGetByEmployeeId(): Unit = withTestApplication(Application::module) {
        id = employeeRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/employees/${id}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testPutByEmployeeId(): Unit = withTestApplication(Application::module) {
        id = employeeRepository.getAll()[0].id
        handleRequest(HttpMethod.Put, "/employees/${id}"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "googleId":"123", "firstName":"Ideal", "lastName":"Petrov", "verified":false}""")
        }.apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testDeleteByEmployeeId(): Unit = withTestApplication(Application::module) {
        id = employeeRepository.getAll()[0].id
        handleRequest(HttpMethod.Delete, "/employees/${id}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testVerifyByEmployeeId(): Unit = withTestApplication(Application::module) {
        id = employeeRepository.getAll()[0].id
        handleRequest(HttpMethod.Put, "/employees/verify/${id}"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"verified":true}""")
        }.apply {
            assertEquals(403, response.status()?.value)
        }
    }

    @Test
    fun testGetReviewsByEmployeeId(): Unit = withTestApplication(Application::module){
        id = employeeRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/employees/${id}/reviews").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGetReviewByReviewIdByEmployeeId(): Unit = withTestApplication(Application::module){
        id = employeeRepository.getAll()[0].id
        //reviewId = reviewRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/employees/${id}/reviews/${reviewId}").apply {
            assertEquals(404, response.status()?.value)
            assertNull(response.content)
        }
    }

    @Test
    fun testGetEmployeeByReviewId(): Unit = withTestApplication(Application::module){
        handleRequest(HttpMethod.Get, "/employees/${reviewId}").apply {
            assertEquals(404, response.status()?.value)
            assertNull(response.content)
        }
    }

    @AfterEach
    fun stopServer(){
        engine.stop(0L, 0L, TimeUnit.MILLISECONDS)
    }

}