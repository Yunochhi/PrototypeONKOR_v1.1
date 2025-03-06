package com.example.prototypeonkor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.APIService.ProtocolFile
import com.example.prototypeonkor.R

class NotificationsAdapter(private val notifications: MutableList<String>) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = notifications[position]
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}