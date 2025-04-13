package com.example.prototypeonkor.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.APIService.SnilsRequest
import  com.example.prototypeonkor.Adapters.MessageAdapter
import com.example.prototypeonkor.Classes.Chat.Chat
import com.example.prototypeonkor.Classes.Chat.Message
import com.example.prototypeonkor.Classes.Chat.MessageDTO
import com.example.prototypeonkor.Classes.User
import com.example.prototypeonkor.Classes.ViewModels.MainViewModel
import com.example.prototypeonkor.Objects.RetrofitInstance
import com.example.prototypeonkor.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TechChatFragment : Fragment(R.layout.fragment_tech_chat) {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var chatRecyclerView: RecyclerView
    private var messageList = listOf<Message>()
    private var activeUserChats = listOf<Chat>()
    private var chat: Chat? = null
    private var user: User? = null
    //private lateinit var viewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView)
        val sendButton = view.findViewById<ImageButton>(R.id.sendButton)
        val messageEditText = view.findViewById<EditText>(R.id.messageEditText)

        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val snils = arguments?.getString("SNILS").toString()

        lifecycleScope.launch {
            //loadMessages()
            //viewModel.initialize()
            loadUser(snils)
            connectUser()
            //getUserActiveChats()
        }



        sendButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    if (activeUserChats.isEmpty())
                    {
                        createChat()
                        val messageText = messageEditText.text.toString().trim()
                        if (messageText.isNotBlank()) {
                            //viewModel.sendMessage(chat!!.id, user!!.id, user!!.fullName, messageText)
                            loadMessages()
                            messageEditText.text.clear()
                            /* messageAdapter.notifyItemInserted(messageList.size)
                             chatRecyclerView.scrollToPosition(messageList.size)*/
                        }
                    }
                }
                catch (e:Exception){
                    Log.e("TechChatFragment.sendButton", "${e.message}")
                }
            }
        }
    }

    private fun connectUser() {
        //viewModel.connectUser(user!!.id)
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
                Log.e("TechChatFragment.sendMessage", "${e.message}")
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
            val chatResponse = RetrofitInstance.apiService.createChat(user!!.id)

            if (!chatResponse.isSuccessful) {
                Log.d("createChatFailed", chatResponse.body().toString())
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
                Log.d("getUserActiveChats", activeChatsResponse.body().toString())
            }
            activeUserChats = activeChatsResponse.body()!!

        }
        catch (e: Exception){
            withContext(Dispatchers.Main) {
                Log.e("TechChatFragment.getUserActiveChats", "${e.message}")
            }
        }
    }
}