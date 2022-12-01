package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDataClass(
    val id: Int, val name: String, val email: String, val phoneNo: String
)
