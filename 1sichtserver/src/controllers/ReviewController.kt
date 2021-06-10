package controllers

import db.models.Review
import db.models.Timeslot
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import org.joda.time.DateTime
import repository.ReviewRepository
import repository.TimeslotRepository
import java.sql.Timestamp
import java.util.*

/**
 * The ReviewController provides URLs as endpoints for the communication with the server
 * @author Stanimir Bozhilov, Martin Zahariev
 */
fun Route.review(reviewRepository: ReviewRepository, timeslotRepository: TimeslotRepository) {

    route("/reviews") {

        /**
         * /reviews/create POST
         * Success: 201
         * Error: 500
         */
        post("/create") {
            val review = call.receive<Review>()
            val createdReview = reviewRepository.create(review)
            if (createdReview == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                call.respond(HttpStatusCode.Created, createdReview)
            }
        }

        /**
         * /reviews/get GET
         * Success: 200
         * Error: 404
         */
        get("/") {
            var reviews = emptyList<Review>()

            if (call.request.queryParameters["keywords"] != null) {
                val keywords: Array<String> = call.request.queryParameters["keywords"]?.split("+")?.toTypedArray()!!
                reviews = reviewRepository.getByKeywords(keywords)
            } else {
                reviews = reviewRepository.getAll()
            }

            if (reviews.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, reviews)
            }
        }

        /**
         * /reviews/get/{reviewId} GET
         * Required: reviewId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{reviewId}") {
            val review = reviewRepository.getById(call.parameters["reviewId"]?.toInt()!!)
            if (review == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, review)
            }
        }

        /**
         * /reviews/put/{reviewId} PUT
         * Required: reviewId = [integer]
         * Success: 200
         * Error: 404
         */
        put("/{reviewId}") {
            val reviewToUpdate = call.receive<Review>()
            val updatedReview = reviewRepository.updateById(call.parameters["reviewId"]?.toInt()!!, reviewToUpdate)
            if (updatedReview == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, updatedReview)
            }
        }

        /**
         * /reviews/delete/{reviewId} DELETE
         * Required: reviewId = [integer]
         * Success: 200
         * Error: 404
         */
        delete("/{reviewId}") {
            val deletedReview = reviewRepository.deleteById(call.parameters["reviewId"]?.toInt()!!)
            if (deletedReview == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, deletedReview)
            }
        }

        /**
         * /reviews/get/{reviewId}/timeslots GET
         * Required: reviewId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{reviewId}/timeslots") {
            val timeslots = timeslotRepository.getAllForReview(call.parameters["reviewId"]?.toInt()!!)
            if (timeslots.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, timeslots)
            }
        }

        /**
         * /reviews/post/{reviewId}/timeslots POST
         * Required: reviewId = [integer]
         * Success: 200
         * Error: 404
         */
        post("/{reviewId}/timeslots") {
            val timeslot = call.receive<Timeslot>()

            val createdTimeslot = timeslotRepository.createForReview(call.parameters["reviewId"]?.toInt()!!, timeslot)
            if (createdTimeslot == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                call.respond(HttpStatusCode.Created, createdTimeslot)
            }
        }

        /**
         * /reviews/get/{reviewId}/timeslots/{timeslotId} GET
         * Required: reviewId = [integer], timeslotId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{reviewId}/timeslots/{timeslotId}") {
            val reviewId = call.parameters["reviewId"]?.toInt()!!
            val timeslotId = call.parameters["timeslotId"]?.toInt()!!
            val timeslot = timeslotRepository.getForReviewById(reviewId, timeslotId)
            if (timeslot == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, timeslot)
            }
        }

        /**
         * /reviews/put/{reviewId}/timeslots/{timeslotId} PUT
         * Required: reviewId = [integer], timeslotId = [integer]
         * Success: 200
         * Error: 404
         */
        put("/{reviewId}/timeslots/{timeslotId}") {
            val reviewId = call.parameters["reviewId"]?.toInt()!!
            val timeslotId = call.parameters["timeslotId"]?.toInt()!!
            val timeslot = call.receive<Timeslot>()

            val updatedTimeslot = timeslotRepository.updateForReviewById(reviewId, timeslotId, timeslot)
            if (updatedTimeslot == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, updatedTimeslot)
            }
        }

        /**
         * /reviews/delete/{reviewId}/timeslots/{timeslotId} DELETE
         * Required: reviewId = [integer], timeslotId = [integer]
         * Success: 200
         * Error: 404
         */
        delete("/{reviewId}/timeslots/{timeslotId}") {
            val reviewId = call.parameters["reviewId"]?.toInt()!!
            val timeslotId = call.parameters["timeslotId"]?.toInt()!!

            val deletedTimeslot = timeslotRepository.deleteForReviewById(reviewId, timeslotId)
            if (deletedTimeslot == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, deletedTimeslot)
            }
        }

        /**
         * /reviews/signUp/{timeslotId}/{studentId} POST
         * Required: timeslotId = [integer], studentId = [integer]
         * Success: 200
         * Error: 404
         */
        post("/signUp/{timeslotId}/{studentId}") {
            val timeslotId = call.parameters["timeslotId"]?.toInt()!!
            val studentId = call.parameters["studentId"]?.toInt()!!

            val signUp = timeslotRepository.signUp(timeslotId, studentId)
            if (signUp == false) {
                call.respond(HttpStatusCode.Forbidden)
            } else {
                call.respond(HttpStatusCode.OK)
            }
        }

        /**
         * /reviews/signOut/{timeslotId}/{studentId} POST
         * Required: timeslotId = [integer], studentId = [integer]
         * Success: 200
         * Error: 404
         */
        post("/signOut/{timeslotId}/{studentId}") {
            val timeslotId = call.parameters["timeslotId"]?.toInt()!!
            val studentId = call.parameters["studentId"]?.toInt()!!

            val signOut = timeslotRepository.signOut(timeslotId, studentId)
            if (signOut == false) {
                call.respond(HttpStatusCode.Forbidden)
            } else {
                call.respond(HttpStatusCode.OK)
            }
        }

        /**
         * /reviews/changeTimeslot/{newTimeslotId}/{studentId} PUT
         * Required: newTimeslotId = [integer], studentId = [integer]
         * Success: 200
         * Error: 404
         */
        put("/changeTimeslot/{oldTimeslotId}/{newTimeslotId}/{studentId}") {
            val oldTimeslotId = call.parameters["oldTimeslotId"]?.toInt()!!
            val newTimeslotId = call.parameters["newTimeslotId"]?.toInt()!!
            val studentId = call.parameters["studentId"]?.toInt()!!

            val changeTimeslot = timeslotRepository.changeTimeslotOfStudent(oldTimeslotId, newTimeslotId, studentId)
            if (changeTimeslot == false) {
                call.respond(HttpStatusCode.Forbidden)
            } else {
                call.respond(HttpStatusCode.OK)
            }
        }

        get("/{reviewId}/students") {
            val reviewId = call.parameters["reviewId"]?.toInt()!!

            val students = reviewRepository.getAllStudents(reviewId)
            if (students.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, students)
            }
        }
    }
}