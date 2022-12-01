package com.example.routing

import com.example.db.DatabaseConnection
import com.example.models.User
import com.example.models.UserDataClass
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select

fun Application.userRoutes() {
    val db=DatabaseConnection.database

    routing {
        get("/users") {
            val users=db.from(User).select()
                .map {
                    val id=it[User.id]
                    val name=it[User.name]
                    val email=it[User.email]
                    val phoneNo=it[User.phoneNo]
                    UserDataClass(id ?: -1,name ?: "",email ?: "",phoneNo ?: "")
                }
            call.respond(users)
        }
    }
}