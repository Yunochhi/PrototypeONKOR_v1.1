package com.example.prototypeonkor.Classes.Chat

import com.example.prototypeonkor.Classes.User
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


data class Message(
    val id: Long = 0,
    val chat: Chat,
    val sender: User,
    val content: String,
    val timestamp: LocalDateTime?
)
