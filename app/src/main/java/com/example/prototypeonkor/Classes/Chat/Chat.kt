package com.example.prototypeonkor.Classes.Chat

import com.example.prototypeonkor.Classes.User
import java.time.LocalDateTime

data class Chat(
    val id: Long = 0,
    val admin: User,
    val user: User,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    var closedAt: LocalDateTime? = null,
    var isActive: Boolean = true
)