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
import com.example.prototypeonkor.APIService.SnilsRequest
import  com.example.prototypeonkor.Adapters.MessageAdapter
import com.example.prototypeonkor.Classes.Chat.Chat
import com.example.prototypeonkor.Classes.Chat.Message
import com.example.prototypeonkor.Classes.Chat.MessageDTO
import com.example.prototypeonkor.Classes.User
import com.example.prototypeonkor.Objects.RetrofitInstance
import com.example.prototypeonkor.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TechChatFragment : Fragment(R.layout.fragment_tech_chat) {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private var messageList = listOf<Message>()
    private var chat: Chat? = null
    private var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView)
        val sendButton = view.findViewById<ImageButton>(R.id.sendButton)
        val messageEditText = view.findViewById<EditText>(R.id.messageEditText)

        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val snils = arguments?.getString("SNILS").toString()

        lifecycleScope.launch {
            /*loadMessages()*/
            loadUser(snils)
        }

        Log.d("snils", snils)

        sendButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {

                createChat()
                if (user == null)
                {
                    Log.e("TechChatFragment", "user is null")
                }
                val messageText = messageEditText.text.toString().trim()

                if (messageText.isNotBlank()) {
                    if (chat != null && user != null && messageText.isNotBlank()) {
                        val newMessage = MessageDTO(chat!!.id, user!!.userId, user!!.fullName, messageText)
                        sendMessage(newMessage)
                        loadMessages()
                        messageEditText.text.clear()
                    } else {
                        Log.e("TechChatFragment", "Chat is null")
                    }
                    /* messageAdapter.notifyItemInserted(messageList.size)
                     chatRecyclerView.scrollToPosition(messageList.size)*/
                }
            }
        }
    }
    private suspend fun loadUser(snils: String) {
        try {
            val snilsRequest = SnilsRequest(snils)
            val response = withContext(Dispatchers.IO) {
                RetrofitInstance.apiService.getUserInfo(snilsRequest)
            }

            if (!response.isSuccessful) {
                return
            }
            user = response.body()
        }
        catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.e("TechChatFragment.loadUser", "${e.message}")
            }
        }
    }
    private suspend fun sendMessage(message: MessageDTO) {
        try {
            withContext(Dispatchers.IO){
                RetrofitInstance.apiService.sendMessage(message)
            }
        }
        catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.e("TechChatFragment.createChat", "${e.message}")
            }
        }
    }
    private suspend fun loadMessages() {
        try {
            val msgResponse = withContext(Dispatchers.IO) {
                RetrofitInstance.apiService.getChatMessages(chat!!.id)
            }
            if (!msgResponse.isSuccessful) {
                return
            }

            messageList = msgResponse.body()!!

            if (messageList.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {
                    messageAdapter = MessageAdapter(messageList)
                    chatRecyclerView.adapter = messageAdapter
                }
            }
        }
        catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.e("TechChatFragment.loadMessages", "${e.message}")
            }
        }
    }
    private suspend fun createChat()
    {
        try {
            val chatResponse = withContext(Dispatchers.IO) {
                RetrofitInstance.apiService.createChat(user!!.userId)
            }

            if (chatResponse.isSuccessful) {
                chat = chatResponse.body()!!
            }
        }
        catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.e("TechChatFragment.createChat", "${e.message}")
            }
        }
    }
}