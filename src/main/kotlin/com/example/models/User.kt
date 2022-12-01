package com.example.models

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object User : Table<Nothing>("user") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val email = varchar("email")
    val phoneNo = varchar("phoneNo")
}