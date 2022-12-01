package com.example.routing

import com.example.db.DatabaseConnection
import com.example.models.User
import com.example.models.UserDataClass
import com.example.models.UserResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.userRoutes() {
    val db = DatabaseConnection.database

    routing {
        get("/users") {
            val users = db.from(User).select().map {
                val id = it[User.id]
                val name = it[User.name]
                val email = it[User.email]
                val phoneNo = it[User.phoneNo]
                UserDataClass(id ?: -1, name ?: "", email ?: "", phoneNo ?: "")
            }
            call.respond(users)
        }

        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1

            val user = db.from(User).select().where { User.id eq id }.map {
                val id = it[User.id]!!
                val name = it[User.name]!!
                val email = it[User.email]!!
                val phoneNo = it[User.phoneNo]!!
                UserDataClass(id = id, name = name, email = email, phoneNo = phoneNo)

            }.firstOrNull()


            println("User= $user")
            if (user == null) {
                call.respond(
                    HttpStatusCode.NotFound, UserResponse(
                        success = false, data = "Could not found user with id= $id"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK, UserResponse(
                        success = true, data = user
                    )
                )
            }
        }

        post("/users") {
            val request = call.receive<UserDataClass>()
            val result = db.insert(User) {
                set(it.name, request.name)
                set(it.email, request.email)
                set(it.phoneNo, request.phoneNo)
            }

            if (result == 1) {
                call.respond(
                    HttpStatusCode.OK, UserResponse(
                        success = true, data = "Values has been successfully inserted"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, UserResponse(
                        success = false, data = "Failed to insert values"
                    )
                )
            }
        }

        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1

            val updateUser = call.receive<UserDataClass>()

            val rowsEffected = db.update(User) {
                set(it.name, updateUser.name)
                set(it.email, updateUser.email)
                set(it.phoneNo, updateUser.phoneNo)
                where {
                    it.id eq id
                }
            }

            if (rowsEffected == 1) {
                call.respond(
                    HttpStatusCode.OK, UserResponse(
                        success = true, data = "User has been updated"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, UserResponse(
                        success = false, data = "User failed to update"
                    )
                )
            }

        }
    }
}