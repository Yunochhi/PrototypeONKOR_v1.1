package com.example.prototypeonkor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.APIService.Notification
import com.example.prototypeonkor.R

class NotificationsAdapter(private val notifications: List<Notification>) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val header: TextView = view.findViewById(R.id.headerTextView)
        val description: TextView = view.findViewById(R.id.DescTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        with(holder)
        {
            header.text = notifications[position].header
            description.text = notifications[position].description
        }
    }
    override fun getItemCount() = notifications.size
}
