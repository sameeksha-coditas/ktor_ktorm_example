package com.example.plugins

import com.example.routing.authenticationRoutes
import com.example.routing.userRoutes
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
    userRoutes()
    authenticationRoutes()
}
