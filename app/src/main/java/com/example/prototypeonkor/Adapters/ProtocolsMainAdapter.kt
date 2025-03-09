package com.example.prototypeonkor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.APIService.ProtocolFile
import com.example.prototypeonkor.R

class ProtocolsMainAdapter(private val protocols: List<ProtocolFile>) : RecyclerView.Adapter<ProtocolsMainAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val investigationName: TextView = view.findViewById(R.id.nameTextView)
        val date: TextView = view.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_protocol_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (protocols.isNotEmpty()) {
            val index = protocols.size - getItemCount() + position
            holder.investigationName.text = protocols[index].info.lpu
            holder.date.text = protocols[index].info.date
        }
    }

    override fun getItemCount(): Int {
        return Math.min(3, protocols.size)
    }
}
