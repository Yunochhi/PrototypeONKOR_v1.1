package com.example.prototypeonkor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.APIService.ProtocolFile
import com.example.prototypeonkor.R

class ProtocolAdapter(private val protocols: List<ProtocolFile>, private val context: Context, private val onFileClick: (String) -> Unit) : RecyclerView.Adapter<ProtocolAdapter.ProtocolViewHolder>() {

    inner class ProtocolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val fileTextView: TextView = itemView.findViewById(R.id.fileTextView)
        val investigationView: TextView = itemView.findViewById(R.id.investigationView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProtocolViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.protocol_info, parent, false)
        return ProtocolViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProtocolViewHolder, position: Int) {
        val protocol = protocols[position]
        with(holder) {
            dateTextView.text = protocol.info.date
            timeTextView.text = protocol.info.time
            titleTextView.text = protocol.info.lpu
            investigationView.text = protocol.info.investigationName
            fileTextView.text = protocol.fileName
            fileTextView.setOnClickListener { onFileClick(protocol.fileName) }
        }
    }

    override fun getItemCount() = protocols.size
}