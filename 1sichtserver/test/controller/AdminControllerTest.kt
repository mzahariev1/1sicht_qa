package controller

import com.typesafe.config.ConfigFactory
import db.models.Administrator
import io.ktor.application.Application
import io.ktor.config.HoconApplicationConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.formUrlEncode
import io.ktor.server.testing.*
import module
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import io.ktor.application.Application.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import repository.AdminRepository

/*import kotlin.test.assertEquals
import kotlin.test.assertNotNull*/

class AdminControllerTest {

    protected lateinit var engine: TestApplicationEngine

    private var adminRepository: AdminRepository = AdminRepository()
    private var id: Int? = null

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
        handleRequest(HttpMethod.Post, "/admins/create") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "googleId":"123", "firstName":"Gosho", "lastName":"Petrov"}""")

        }.apply {
            //var statusValue = response.content
            assertEquals(201, response.status()!!.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGet(): Unit = withTestApplication(Application::module) {
        handleRequest(HttpMethod.Get, "/admins/").apply {
            //var statusValue = response.status()
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testGetById(): Unit = withTestApplication(Application::module) {
        id = adminRepository.getAll()[0].id
        handleRequest(HttpMethod.Get, "/admins/${id}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testUpdateById(): Unit = withTestApplication(Application::module) {
        id = adminRepository.getAll()[0].id
        handleRequest(HttpMethod.Put, "/admins/${id}"){
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("""{"id":1, "googleId":"123", "firstName":"Ideal", "lastName":"Petrov"}""")
        }.apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @Test
    fun testDeleteById(): Unit = withTestApplication(Application::module) {
        id = adminRepository.getAll()[0].id
        handleRequest(HttpMethod.Delete, "/admins/${id}").apply {
            assertEquals(200, response.status()?.value)
            assertNotNull(response.content)
        }
    }

    @AfterEach
    fun stopServer(){
        engine.stop(0L, 0L, TimeUnit.MILLISECONDS)
    }

}