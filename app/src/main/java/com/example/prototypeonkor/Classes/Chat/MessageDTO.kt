package com.example.prototypeonkor.Classes.Chat

data class MessageDTO(
    val chatId: Long,
    val senderId: Long,
    val senderUsername: String,
    val content: String,
)