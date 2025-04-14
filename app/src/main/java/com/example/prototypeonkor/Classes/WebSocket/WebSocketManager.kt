package com.example.prototypeonkor.Classes.WebSocket

import android.util.Log
import com.example.prototypeonkor.Classes.Chat.ChatNotification
import com.example.prototypeonkor.Classes.Chat.MessageDTO
import com.example.prototypeonkor.Classes.Requests.CheckSubscriptionRequest
import com.example.prototypeonkor.Classes.Requests.CloseChatRequest
import com.example.prototypeonkor.Classes.Requests.CreateChatRequest
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketManager(
    private val userId: Long,
    private val isAdmin: Boolean
) {
    companion object {
        private const val WS_URL = "ws://10.0.2.2:8080/ws"
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    private val client = OkHttpClient.Builder()
        .pingInterval(10, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private val gson = Gson()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    interface WebSocketListener {
        fun onMessageReceived(message: MessageDTO)
        fun onNotificationReceived(notification: ChatNotification)
        fun onConnected()
        fun onError(error: String)
    }

    private var listener: WebSocketListener? = null

    fun setListener(listener: WebSocketListener) {
        this.listener = listener
    }

    fun connect() {
        val request = Request.Builder()
            .url(WS_URL)
            .addHeader("userId", userId.toString())
            .addHeader("role", if (isAdmin) "ADMIN" else "USER")
            .build()

        webSocket = client.newWebSocket(request, object : okhttp3.WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                listener?.onConnected()
                setupSubscriptions()
                sendConnectRequest()
                Log.d("WebSocket", "Connected to $WS_URL")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                processIncomingMessage(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                listener?.onError("Connection closed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                listener?.onError("Connection error: ${t.message}")
                scheduleReconnect()
            }
        })
    }

    private fun setupSubscriptions() {
        subscribeToUserMessages()
        subscribeToNotifications()
    }

    private fun subscribeToUserMessages() {
        sendStompFrame(
            command = "SUBSCRIBE",
            headers = mapOf(
                "id" to "sub-user-$userId-messages",
                "destination" to "/user/queue/messages"
            )
        )
    }

    private fun subscribeToNotifications() {
        sendStompFrame(
            command = "SUBSCRIBE",
            headers = mapOf(
                "id" to "sub-user-$userId-notifications",
                "destination" to "/user/queue/notifications"
            )
        )
    }

    private fun processIncomingMessage(text: String) {
        try {
            when {
                text.contains("\"chatId\"") -> handleMessage(text)
                text.contains("\"type\"") -> handleNotification(text)
            }
        } catch (e: Exception) {
            listener?.onError("Message processing failed: ${e.message}")
        }
    }

    private fun handleMessage(text: String) {
        val message = gson.fromJson(text, MessageDTO::class.java)
        listener?.onMessageReceived(message)
    }

    private fun handleNotification(text: String) {
        val notification = gson.fromJson(text, ChatNotification::class.java)
        listener?.onNotificationReceived(notification)
    }

    fun sendConnectRequest() {
        sendStompFrame(
            command = "CONNECT",
            headers = mapOf(
                "accept-version" to "1.2",
                "heart-beat" to "10000,10000"
            )
        )
    }

    fun sendMessage(message: MessageDTO) {
        sendToEndpoint("/app/chat.sendMessage", message)
    }

    fun createChat(userId: Long) {
        sendToEndpoint("/app/chat.create", CreateChatRequest(userId))
    }

    fun closeChat(chatId: Long) {
        sendToEndpoint("/app/chat.close", CloseChatRequest(chatId))
    }

    fun checkSubscription(userId: Long) {
        sendToEndpoint("/app/chat.checkSubscription", CheckSubscriptionRequest(userId))
    }

    private fun sendToEndpoint(destination: String, payload: Any) {
        scope.launch {
            try {
                val json = gson.toJson(payload)
                sendStompFrame(
                    command = "SEND",
                    headers = mapOf("destination" to destination),
                    body = json
                )
            } catch (e: Exception) {
                listener?.onError("Failed to send message: ${e.message}")
            }
        }
    }

    private fun sendStompFrame(
        command: String,
        headers: Map<String, String> = emptyMap(),
        body: String = ""
    ) {
        val frame = buildString {
            append("$command\n")
            headers.forEach { (key, value) -> append("$key:$value\n") }
            append("\n$body\u0000")
        }
        webSocket?.send(frame)
    }

    private fun scheduleReconnect() {
        scope.launch {
            delay(5000)
            connect()
        }
    }

    fun disconnect() {
        webSocket?.close(NORMAL_CLOSURE_STATUS, "Normal closure")
        scope.cancel()
    }
}