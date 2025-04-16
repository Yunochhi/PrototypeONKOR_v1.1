package com.example.prototypeonkor.Classes

import com.example.prototypeonkor.Enums.Gender
import com.example.prototypeonkor.Enums.UserRole
import java.sql.Date
import java.time.LocalDate

data class User(
    val id: Long,
    val snils:String,
    val age:Int,
    val fullName:String,
    val gender:Gender,
    val city:String,
    val address:String,
    val height:Int,
    val phoneNumber:String,
    val bloodGroup:String,
    val role: UserRole,
    val dateOfBirthday: LocalDate,
    //var isOnline: Boolean,
    //val activeChatsCount: Int
)