package com.example

import com.example.models.User
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

//    database.insert(User){
//        set(it.name,"sameeksha")
//        set(it.email,"sameeksha@gmail.com")
//        set(it.phoneNo,"9179460939")
//    }
//    database.insert(User){
//        set(it.name,"john")
//        set(it.email,"john@gmail.com")
//        set(it.phoneNo,"9179460939")
//    }
//    database.insert(User){
//        set(it.name,"mary")
//        set(it.email,"mary@gmail.com")
//        set(it.phoneNo,"9179460939")
//    }


//    database.delete(User){
//        it.id eq 7
//    }
//    database.delete(User){
//        it.id eq 8
//    }
//    database.delete(User){
//        it.id eq 9
//    }
//    database.delete(User){
//        it.id eq 10
//    }
//    var users=database.from(User).select()

//    for(row in users){
//        println("${row[User.id]}: ${row[User.name]}")
//    }
    configureSerialization()
    configureRouting()
}
