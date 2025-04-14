package com.example.prototypeonkor.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import  com.example.prototypeonkor.Adapters.MessageAdapter
import com.example.prototypeonkor.Classes.Chat.Chat
import com.example.prototypeonkor.Classes.Chat.ChatNotification
import com.example.prototypeonkor.Classes.Chat.Message
import com.example.prototypeonkor.Classes.Chat.MessageDTO
import com.example.prototypeonkor.Classes.Requests.SnilsRequest
import com.example.prototypeonkor.Classes.User
import com.example.prototypeonkor.Classes.WebSocket.WebSocketManager
import com.example.prototypeonkor.Objects.RetrofitInstance
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.FragmentTechChatBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TechChatFragment : Fragment(R.layout.fragment_tech_chat), WebSocketManager.WebSocketListener {
    private lateinit var messageAdapter: MessageAdapter

    private var messageList = listOf<Message>()
    private var activeUserChats = listOf<Chat>()
    private var chat: Chat? = null
    private var user: User? = null
    private lateinit var binding: FragmentTechChatBinding
    private lateinit var webSocketManager: WebSocketManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTechChatBinding.bind(view)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageAdapter = MessageAdapter(messageList)
        binding.chatRecyclerView.adapter = messageAdapter

        setupWebSocket()

        binding.sendButton.setOnClickListener {
            lifecycleScope.launch {
                val messageText = binding.messageEditText.text.toString().trim()
                if (messageText.isNotBlank()) {
                    webSocketManager.sendMessage(MessageDTO(chat!!.id, user!!.id, user!!.fullName, messageText))
                    loadMessages()
                    binding.messageEditText.text.clear()
                }
            }
        }
    }
    private fun setupWebSocket() {
        val snils = arguments?.getString("SNILS") ?: ""

        lifecycleScope.launch {
            loadUser(snils) // Загрузка данных пользователя
            user?.let {
                webSocketManager = WebSocketManager(it.id, isAdmin = false).apply {
                    setListener(this@TechChatFragment)
                    connect()
                }
                loadInitialData()
            }
        }
    }
    private fun loadInitialData() {
        lifecycleScope.launch {
            getUserActiveChats()
            chat = getOrCreateChatId()
            loadMessages()
        }
    }
    private suspend fun getOrCreateChatId(): Chat {
        return if (activeUserChats.isNotEmpty()) {
            activeUserChats.first()
        }
        else {
            createChat()
            activeUserChats.first()
        }
    }
    private suspend fun loadUser(snils: String) {
        try {
            val snilsRequest = SnilsRequest(snils)
            val userResponse = RetrofitInstance.apiService.getUserInfo(snilsRequest)

            if (!userResponse.isSuccessful) {
                Log.d("loadUserFailed", userResponse.errorBody().toString())
                return
            }
            user = userResponse.body()
        }
        catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.e("TechChatFragment.loadUser", "${e.message}")
            }
        }
    }
    private suspend fun loadMessages() {
        try {
            val msgResponse = RetrofitInstance.apiService.getChatMessages(chat!!.id)

            if (!msgResponse.isSuccessful) {
                return
            }

            messageList = msgResponse.body()!!

            if (messageList.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {
                    messageAdapter = MessageAdapter(messageList)
                    binding.chatRecyclerView.adapter = messageAdapter
                }
            }
        }
        catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.e("TechChatFragment.loadMessages", "${e.message}")
            }
        }
    }
    private suspend fun createChat() {
        try {
            val chatResponse = RetrofitInstance.apiService.createChat(user!!.id)

            if (!chatResponse.isSuccessful) {
                Log.d("createChatFailed", chatResponse.errorBody().toString())
                return
            }
            chat = chatResponse.body()!!
        }
        catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.e("TechChatFragment.createChat", "${e.message}")
            }
        }
    }
    private suspend fun getUserActiveChats() {
        try {
            val activeChatsResponse = RetrofitInstance.apiService.getUserActiveChats(user!!.id)

            if (!activeChatsResponse.isSuccessful) {
                Log.d("getUserActiveChatsFailed", activeChatsResponse.errorBody().toString())
                return
            }
            activeUserChats = activeChatsResponse.body()!!

        }
        catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.e("TechChatFragment.getUserActiveChats", "${e.message}")
            }
        }
    }

    override fun onMessageReceived(message: MessageDTO) {}
    override fun onNotificationReceived(notification: ChatNotification) {}
    override fun onConnected() {}
    override fun onError(error: String) {}
}
