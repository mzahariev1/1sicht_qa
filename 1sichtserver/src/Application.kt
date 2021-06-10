import controllers.admin
import controllers.employee
import controllers.review
import controllers.student
import db.Database
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.gson.*
import java.text.DateFormat
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import repository.*


fun main(args: Array<String>) {
    embeddedServer(Netty, 8080, watchPaths = listOf("ApplicationKt"), module = Application::module).start()

    /*install(CallLogging)
    install(DefaultHeaders)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateTimeFormat.patternForStyle("SM", null))
            setPrettyPrinting()
        }
    }

    Database.setupDB()

    val adminRepository = AdminRepository()
    val employeeRepository = EmployeeRepository()
    val studentRepository = StudentRepository()
    val reviewRepository = ReviewRepository()
    val timeslotRepository = TimeslotRepository()

    install(Routing) {
        admin(adminRepository)
        employee(employeeRepository)
        student(studentRepository)
        review(reviewRepository, timeslotRepository)
    }*/
}

fun Application.module() {
    install(CallLogging)
    install(DefaultHeaders)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            serializeNulls()
        }
    }

    Database.setupDB()

    val adminRepository = AdminRepository()
    val employeeRepository = EmployeeRepository()
    val studentRepository = StudentRepository()
    val reviewRepository = ReviewRepository()
    val timeslotRepository = TimeslotRepository()

    install(Routing) {
        admin(adminRepository)
        employee(employeeRepository)
        student(studentRepository)
        review(reviewRepository, timeslotRepository)
    }
}