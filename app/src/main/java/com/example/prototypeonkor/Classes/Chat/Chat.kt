package com.example.prototypeonkor.Classes.Chat

import com.example.prototypeonkor.Classes.User
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDateTime

data class Chat(
    val id: Long = 0,
    val admin: User,
    val user: User,

    val createdAt: LocalDateTime,

    var closedAt: LocalDateTime? = null,
    var isActive: Boolean = true
)