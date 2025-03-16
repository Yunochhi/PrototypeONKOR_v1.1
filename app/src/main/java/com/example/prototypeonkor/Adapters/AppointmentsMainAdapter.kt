package com.example.prototypeonkor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.Class.Appointment
import com.example.prototypeonkor.R

class AppointmentsMainAdapter(private val appointments: List<Appointment>) : RecyclerView.Adapter<AppointmentsMainAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val investigationName: TextView = view.findViewById(R.id.nameTextView)
        val date: TextView = view.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_visits_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val appoinment = appointments[position]
        with(holder)
        {
            investigationName.text = "Исследование: ${appoinment.investigationName}"
            date.text = "Дата: ${appoinment.date}"
        }
    }

    override fun getItemCount() = appointments.take(3).size
}
