package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.User
import com.example.models.UserDataClass
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.ktorm.dsl.insert

fun Application.authenticationRoutes(){
    val db=DatabaseConnection.database

    routing {

    }
}