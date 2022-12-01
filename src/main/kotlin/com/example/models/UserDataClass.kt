package com.example.models

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserDataClass(
    val id: Int, val name: String, val email: String, val phoneNo: String, val password: String
){
    fun hashedPassword():String{
        return BCrypt.hashpw(password,BCrypt.gensalt())
    }

    fun isValidCredentials():Boolean{
        return name.length>=3 && password.length>=6
    }
}
