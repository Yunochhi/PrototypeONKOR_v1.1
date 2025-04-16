package com.example.prototypeonkor.Classes.WebSocket

import android.util.Log
import com.example.prototypeonkor.Classes.Chat.ChatNotification
import com.example.prototypeonkor.Classes.Chat.MessageDTO
import com.example.prototypeonkor.Classes.Requests.CheckSubscriptionRequest
import com.example.prototypeonkor.Classes.Requests.CloseChatRequest
import com.example.prototypeonkor.Classes.Requests.ConnectRequest
import com.example.prototypeonkor.Classes.Requests.CreateChatRequest
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.internal.http2.Http2Reader.Companion.logger
import java.util.concurrent.TimeUnit

class WebSocketManager(
    private val userId: Long,
    private val isAdmin: Boolean
) {
    companion object {
        private const val WS_URL = "ws://10.0.2.2:8080/ws/websocket"
        private const val NORMAL_CLOSURE_STATUS = 1000
        private val gson = Gson()
    }

    private val client = OkHttpClient.Builder()
        .pingInterval(10, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var isStompConnected = false

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
            .build()

        webSocket = client.newWebSocket(request, object : okhttp3.WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                sendStompConnect()
                Log.d("WebSocket", "Connected to $WS_URL")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                processStompFrame(text)
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

    private fun handleMessage(text: String) {
        try {
            val message = gson.fromJson(text, MessageDTO::class.java)
            listener?.onMessageReceived(message)
        } catch (e: Exception) {
            listener?.onError("Message parsing error: ${e.message}")
        }
    }

    private fun handleNotification(text: String) {
        try {
            val notification = gson.fromJson(text, ChatNotification::class.java)
            listener?.onNotificationReceived(notification)
        } catch (e: Exception) {
            listener?.onError("Notification parsing error: ${e.message}")
        }
    }

    private fun sendToEndpoint(destination: String, payload: Any)   {
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

    private fun processStompFrame(frame: String) {
        val lines = frame.split("\n")
        when (lines[0]) {
            "CONNECTED" -> handleConnected()
            "MESSAGE" -> handleMessageFrame(lines)
            "ERROR" -> handleErrorFrame(lines)
            else -> logger.warning("Unknown STOMP frame: ${lines[0]}")
        }
    }

    private fun handleConnected() {
        isStompConnected = true
        listener?.onConnected()
        setupSubscriptions()
        sendConnectRequest()
    }

    private fun handleMessageFrame(lines: List<String>) {
        val body = lines.lastOrNull { it.isNotEmpty() } ?: return
        when {
            body.contains("\"chatId\"") -> handleMessage(body)
            body.contains("\"type\"") -> handleNotification(body)
        }
    }

    private fun handleErrorFrame(lines: List<String>) {
        val errorMessage = lines.find { it.startsWith("message:") }?.substringAfter(":")
            ?: "Unknown error"
        listener?.onError("STOMP error: $errorMessage")
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

    private fun sendStompConnect() {
        sendStompFrame(
            command = "CONNECT",
            headers = mapOf(
                "userId" to userId.toString(),
                "role" to if (isAdmin) "ADMIN" else "USER",
                "accept-version" to "1.2",
                "heart-beat" to "10000,10000"
            )
        )
    }

    private fun sendConnectRequest() {
        sendToEndpoint("/app/chat.connect", ConnectRequest(userId))
    }

    private fun sendStompFrame(command: String, headers: Map<String, String> = emptyMap(), body: String = "") {
        if (!isStompConnected && command != "CONNECT") {
            listener?.onError("Trying to send $command before STOMP connection")
            return
        }

        val frame = buildString {
            append("$command\n")
            headers.forEach { (key, value) -> append("$key:$value\n") }
            append("\n$body\u0000")
        }
        webSocket?.send(frame) ?: run {
            listener?.onError("WebSocket is not initialized")
        }
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