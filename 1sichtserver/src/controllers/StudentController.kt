package controllers

import db.models.Student
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import repository.StudentRepository

/**
 * The ReviewController provides URLs as endpoints for the communication with the server
 * @author Stanimir Bozhilov, Martin Zahariev
 */
fun Route.student(studentRepository: StudentRepository) {

    route("/students") {

        /**
         * /students/create POST
         * Success: 201
         * Error: 500
         */
        post("/create") {
            val student = call.receive<Student>()

            val createdStudent = studentRepository.create(student)
            if (createdStudent == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                call.respond(HttpStatusCode.Created, createdStudent)
            }
        }

        /**
         * /students GET
         * Success: 200
         * Error: 404
         */
        get("/") {
            val students = studentRepository.getAll()
            if (students.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, students)
            }
        }

        /**
         * /students/{studentId} GET
         * Required: studentId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{studentId}") {
            val student = studentRepository.getById(call.parameters["studentId"]?.toInt()!!)
            if (student == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, student)
            }
        }

        /**
         * /students/{studentId} PUT
         * Required: studentId = [integer]
         * Success: 200
         * Error: 404
         */
        put("/{studentId}") {
            val studentToUpdate = call.receive<Student>()
            val updatedStudent = studentRepository.updateById(call.parameters["studentId"]?.toInt()!!, studentToUpdate)
            if (updatedStudent == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, updatedStudent)
            }
        }

        /**
         * /students/{studentId} DELETE
         * Required: studentId = [integer]
         * Success: 200
         * Error: 404
         */
        delete("/{studentId}") {
            val deletedStudent = studentRepository.deleteById(call.parameters["studentId"]?.toInt()!!)
            if (deletedStudent == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, deletedStudent)
            }
        }

        /**
         * /students/{studentId}/reviews GET
         * Required: studentId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{studentId}/reviews") {
            val reviews = studentRepository.getReviewsForStudent(call.parameters["studentId"]?.toInt()!!)
            call.respond(HttpStatusCode.OK, reviews)
        }

        /**
         * /students/{studentId}/timeslot GET
         * Required: studentId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{studentId}/timeslots") {
            val timeslots = studentRepository.getTimeslotsForStudent(call.parameters["studentId"]?.toInt()!!)
            call.respond(HttpStatusCode.OK, timeslots)
        }

        /**
         * /{reviewId} GET
         * Required: reviewId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{reviewId}"){
            val students = studentRepository.getStudentsForReview(call.parameters["reviewId"]?.toInt()!!)
            if (students == null){
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, students)
            }
        }

        /**
         * /{timeslotId} GET
         * Required: timeslotId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{timeslotId}"){
            val students = studentRepository.getStudentsForTimeslot(call.parameters["timeslotId"]?.toInt()!!)
            if (students == null){
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, students)
            }
        }

        /**
         * /{studentId}/{reviewId} GET
         * Required: timeslotId = [integer], reviewId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{studentId}/{reviewId}"){
            val timeslot = studentRepository.getTimeslotInReview(call.parameters["studentId"]?.toInt()!!, call.parameters["reviewId"]?.toInt()!!)
            if (timeslot == null){
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, timeslot)
            }
        }
    }
}