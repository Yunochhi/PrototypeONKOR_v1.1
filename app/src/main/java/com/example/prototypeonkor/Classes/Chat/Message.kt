package com.example.prototypeonkor.Classes.Chat

import com.example.prototypeonkor.Classes.User
import java.time.LocalDateTime

data class Message(
    val chat: Chat,
    val sender: User,
    val content: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
