package com.example.prototypeonkor.Fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import  com.example.prototypeonkor.Adapters.MessageAdapter
import com.example.prototypeonkor.Classes.Message
import com.example.prototypeonkor.R

class TechChatFragment : Fragment(R.layout.fragment_tech_chat) {

    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<Message>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatRecyclerView = view.findViewById<RecyclerView>(R.id.chatRecyclerView)
        val sendButton = view.findViewById<ImageButton>(R.id.sendButton)
        val messageEditText = view.findViewById<EditText>(R.id.messageEditText)

        messageAdapter = MessageAdapter(messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatRecyclerView.adapter = messageAdapter

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()

            if (messageText.isNotBlank()) {
                val newMessage = Message(text = messageText, isUser = true)
                messageList.add(newMessage)
                messageAdapter.notifyItemInserted(messageList.size - 1)
                chatRecyclerView.scrollToPosition(messageList.size - 1)
                messageEditText.text.clear()
            }
        }
    }
}