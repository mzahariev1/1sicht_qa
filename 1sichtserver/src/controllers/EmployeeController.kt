package controllers

import db.models.Employee
import io.ktor.application.call
import io.ktor.content.TextContent
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import repository.EmployeeRepository

/**
 * The EmployeeController provides URLs as endpoints for the communication with the server
 * @author Stanimir Bozhilov, Martin Zahariev
 */
fun Route.employee(employeeRepository: EmployeeRepository) {

    route("/employees") {

        /**
         * /employees/create POST
         * Success: 201
         * Error: 500
         */
        post("/create") {
            val employee = call.receive<Employee>()

            val createdEmployee = employeeRepository.create(employee)
            if (createdEmployee == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                call.respond(HttpStatusCode.Created, createdEmployee)
            }
        }

        /**
         * /employees GET
         * Success: 200
         * Error: 404
         */
        get("/") {
            val employees = employeeRepository.getAll()
            if (employees.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, employees)
            }
        }

        /**
         * /employees/{employeeId} GET
         * Required: employeeId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{employeeId}") {
            val employee = employeeRepository.getById(call.parameters["employeeId"]?.toInt()!!)
            if (employee == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, employee)
            }
        }

        /**
         * /employees/{employeeId} PUT
         * Required: employeeId = [integer]
         * Success: 200
         * Error: 404
         */
        put("/{employeeId}") {
            val employeeToUpdate = call.receive<Employee>()
            val updatedEmployee = employeeRepository.updateById(call.parameters["employeeId"]?.toInt()!!, employeeToUpdate)
            if (updatedEmployee == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, updatedEmployee)
            }
        }

        /**
         * /employees/{employeeId} DELETE
         * Required: employeeId = [integer]
         * Success: 200
         * Error: 404
         */
        delete("/{employeeId}") {
            val deletedEmployee = employeeRepository.deleteById(call.parameters["employeeId"]?.toInt()!!)
            if (deletedEmployee == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, deletedEmployee)
            }
        }

        /**
         * /employees/verify/{employeeId} PUT
         * Required: employeeId = [integer]
         * Success: 200
         * Error: 403
         */
        put("/verify/{employeeId}") {
            val verificationSuccessful = employeeRepository.verifyById(call.parameters["employeeId"]?.toInt()!!)
            if (!verificationSuccessful) {
                call.respond(HttpStatusCode.Forbidden)
            } else {
                call.respond(HttpStatusCode.OK)
            }
        }

        /**
         * /employees/{employeeId}/reviews GET
         * Required: employeeId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{employeeId}/reviews") {
            val reviewsForEmployee = employeeRepository.getReviewsByEmployee(call.parameters["employeeId"]?.toInt()!!)
            if (reviewsForEmployee.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, reviewsForEmployee)
            }
        }

        /**
         * /employees/{employeeId}/reviews/{reviewId} GET
         * Required: employeeId = [integer], reviewsId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{employeeId}/reviews/{reviewId}") {
            val employeeId = call.parameters["employeeId"]?.toInt()!!
            val reviewId = call.parameters["reviewId"]?.toInt()!!
            val review = employeeRepository.getReviewByIdByEmployee(employeeId, reviewId)

            if (review == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, review)
            }
        }

        /**
         * /{reviewId} GET
         * Required: reviewId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{reviewId}"){
            val reviewId = call.parameters["reviewId"]?.toInt()!!
            val employee = employeeRepository.getEmployeeByReview(reviewId)

            if (employee == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, employee)
            }
        }
    }
}