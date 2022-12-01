package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.User
import com.example.models.UserDataClass
import com.example.models.UserResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Application.userRoutes() {
    val db = DatabaseConnection.database

    routing {
        get("/users") {
            val users = db.from(User).select().map {
                val id = it[User.id]
                val name = it[User.name]
                val email = it[User.email]
                val phoneNo = it[User.phoneNo]
                val password=it[User.password]
                UserDataClass(id ?: -1, name ?: "", email ?: "", phoneNo ?: "",password ?: "")
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
                val password= it[User.password]!!
                UserDataClass(id = id, name = name, email = email, phoneNo = phoneNo, password=password)

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

        post("/register") {
            val request = call.receive<UserDataClass>()

            if(!request.isValidCredentials()){
                call.respond(HttpStatusCode.BadRequest,
                UserResponse(success = false, data = "Username should be greater than or equal to 3 and password should be greater than or equal to 6"))
                return@post
            }

            val username=request.email
            //Check if user already exists
            val user=db.from(User).select()
                .where{User.email eq username}
                .map { it[User.email] }
                .firstOrNull()


            if(user!=null){
                call.respond(HttpStatusCode.BadRequest,
                UserResponse(success = false,data="User already exists,please try with different email address"))
                return@post
            }

            val result = db.insert(User) {
                set(it.name, request.name)
                set(it.email, request.email)
                set(it.phoneNo, request.phoneNo)
                set(it.password, request.hashedPassword())
            }

            if (result == 1) {
                call.respond(
                    HttpStatusCode.Created, UserResponse(
                        success = true, data = "User has been successfully created"
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

        post("/login") {
            val request = call.receive<UserDataClass>()
            if(!request.isValidCredentials()){
                call.respond(HttpStatusCode.BadRequest,
                    UserResponse(success = false, data = "Username should be greater than or equal to 3 and password should be greater than or equal to 6"))
                return@post
            }

            val username=request.email
            val password=request.password

            //Check if user exists
            val user=db.from(User).select()
                .where{User.email eq username}
                .map {
                    val id= it[User.id]!!
                    val username=it[User.name]!!
                    val password=it[User.password]!!
                    val email=it[User.email]!!
                    val phoneNo=it[User.phoneNo]!!

                    UserDataClass(id = id, name = username, password=password, email = email,phoneNo=phoneNo)
                }.firstOrNull()

            if(user == null){
                call.respond(HttpStatusCode.BadRequest,
                    UserResponse(success = false,data="Invalid Username or password"))
                return@post
            }

            val doesPasswordMatch=BCrypt.checkpw(password, user.password)
            if(!doesPasswordMatch){
                call.respond(HttpStatusCode.BadRequest,
                    UserResponse(success = false,data="Invalid Username or password"))
                return@post
            }

            call.respond(HttpStatusCode.OK,
            UserResponse(success = true, data = "User successfully logged in!"))

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

        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val rowsEffected = db.delete(User) {
                it.id eq id
            }

            if (rowsEffected == 1) {
                call.respond(
                    HttpStatusCode.OK, UserResponse(
                        success = true, data = "User has been deleted"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest, UserResponse(
                        success = false, data = "User failed to delete"
                    )
                )
            }
        }
    }
}