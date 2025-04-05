package com.example.prototypeonkor.Classes

import com.example.prototypeonkor.Enums.Gender

data class User(
    val snils:String,
    val age:Int,
    val fullName:String,
    val gender:Gender,
    val city:String,
    val address:String,
    val height:Int,
    val phoneNumber:String,
    val bloodGroup:String
)