package com.example.prototypeonkor.Classes.Chat

import java.time.LocalDateTime

data class MessageDTO(
    val chatId: Long,
    val senderId: Long,
    val senderUsername: String,
    val content: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)