package com.example.prototypeonkor.Classes.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.prototypeonkor.APIService.ConnectRequest
import com.example.prototypeonkor.Classes.Chat.ChatNotification
import com.example.prototypeonkor.Classes.Chat.MessageDTO

class MainViewModel : ViewModel() {
    companion object {
        const val SOCKET_URL = "ws://10.0.2.2:8080/ws"
        const val MESSAGES_TOPIC = "/user/queue/messages"
        const val NOTIFICATIONS_TOPIC = "/user/queue/notifications"
        const val SEND_MESSAGE_ENDPOINT = "/app/chat.sendMessage"
        const val CONNECT_ENDPOINT = "/app/chat.connect"
    }

    private val _messages = MutableLiveData<MessageDTO>()
    val messages: LiveData<MessageDTO> = _messages

    private val _notifications = MutableLiveData<ChatNotification>()
    val notifications: LiveData<ChatNotification> = _notifications

    private val _connectionStatus = MutableLiveData<Boolean>()
    val connectionStatus: LiveData<Boolean> = _connectionStatus

    //private lateinit var stompClient: SpringStompClient

    /*fun initialize() {
        stompClient = SpringStompClient().apply {
            connect(
                url = SOCKET_URL,
                onConnected = {
                    _connectionStatus.postValue(true)
                    setupSubscriptions()
                },
                onError = { error ->
                    _connectionStatus.postValue(false)
                    Log.e("WebSocket", "Connection error", error)
                }
            )
        }
    }

    private fun setupSubscriptions() {
        stompClient.subscribe(MESSAGES_TOPIC, ::handleMessage)
        stompClient.subscribe(NOTIFICATIONS_TOPIC, ::handleNotification)
    }

    fun connectUser(userId: Long) {
        stompClient.send(CONNECT_ENDPOINT, ConnectRequest(userId))
    }

    fun sendMessage(chatId: Long, senderId: Long, senderUsername: String, content: String) {
        val message = MessageDTO(
            chatId = chatId,
            senderId = senderId,
            senderUsername = senderUsername,
            content = content
        )
        stompClient.send(SEND_MESSAGE_ENDPOINT, message)
    }

    private fun handleMessage(json: String) {
        try {
            val message = stompClient.objectMapper.readValue(json, MessageDTO::class.java)
            _messages.postValue(message)
        } catch (e: Exception) {
            Log.e("WebSocket", "Error parsing message", e)
        }
    }

    private fun handleNotification(json: String) {
        try {
            val notification = stompClient.objectMapper.readValue(json, ChatNotification::class.java)
            _notifications.postValue(notification)
        } catch (e: Exception) {
            Log.e("WebSocket", "Error parsing notification", e)
        }
    }

    override fun onCleared() {
        stompClient.disconnect()
        super.onCleared()
    }*/
}