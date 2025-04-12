package com.example.prototypeonkor.Classes

import com.example.prototypeonkor.Enums.Gender
import java.sql.Date

data class User(
    val userId: Long,
    val snils:String,
    val age:Int,
    val fullName:String,
    val gender:Gender,
    val city:String,
    val address:String,
    val height:Int,
    val phoneNumber:String,
    val bloodGroup:String,
    val dateOfBirthday: Date,
    val activeChatsCount: Int,
    val isOnline: Boolean
)