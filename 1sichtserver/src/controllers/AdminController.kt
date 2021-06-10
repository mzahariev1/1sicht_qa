package controllers

import db.models.Administrator
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import repository.AdminRepository

/**
 * The AdminController provides URLs as endpoints for the communication with the server
 * @author Stanimir Bozhilov, Martin Zahariev
 */
fun Route.admin(adminRepository: AdminRepository) {

    route("/admins") {

        /**
         * /admins/create POST
         * Success: 201
         * Error: 500
         */
        post("/create") {
            val admin = call.receive<Administrator>()
            val createdAdmin = adminRepository.create(admin)
            if (createdAdmin == null) {
                call.respond(HttpStatusCode.InternalServerError)
            } else {
                call.respond(HttpStatusCode.Created, createdAdmin)
            }
        }

        /**
         * /admins/get GET
         * Success: 200
         * Error: 404
         */
        get("/") {
            val admins = adminRepository.getAll()
            if (admins.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, admins)
            }
        }

        /**
         * /admins/get/{adminId} GET
         * Required: adminId = [integer]
         * Success: 200
         * Error: 404
         */
        get("/{adminId}") {
            val admin = adminRepository.getById(call.parameters["adminId"]?.toInt()!!)
            if (admin == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, admin)
            }
        }

        /**
         * /admins/put/{adminId} PUT
         * Required: adminId = [integer]
         * Success: 200
         * Error: 404
         */
        put("/{adminId}") {
            val adminToUpdate = call.receive<Administrator>()
            val updatedAdmin = adminRepository.updateById(call.parameters["adminId"]?.toInt()!!, adminToUpdate)
            if (updatedAdmin == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, updatedAdmin)
            }
        }

        /**
         * /admins/delete/{adminId} DELETE
         * Required: adminId = [integer]
         * Success: 200
         * Error: 404
         */
        delete("/{adminId}") {
            val deletedAdmin = adminRepository.deleteById(call.parameters["adminId"]?.toInt()!!)
            if (deletedAdmin == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(HttpStatusCode.OK, deletedAdmin)
            }
        }
    }
}