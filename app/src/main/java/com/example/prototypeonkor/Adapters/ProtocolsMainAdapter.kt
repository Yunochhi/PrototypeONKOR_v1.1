package com.example.prototypeonkor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.APIService.ProtocolFile
import com.example.prototypeonkor.R

class ProtocolsMainAdapter(private val protocols: List<ProtocolFile>) : RecyclerView.Adapter<ProtocolsMainAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val investigationName: TextView = view.findViewById(R.id.nameTextView)
        val date: TextView = view.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_protocol_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val protocol = protocols[position]
        with(holder)
        {
            investigationName.text = "ЛПУ: ${protocol.info.lpu}"
            date.text = "Дата: ${protocol.info.date}"
        }
    }

    override fun getItemCount() = protocols.take(3).size
}
