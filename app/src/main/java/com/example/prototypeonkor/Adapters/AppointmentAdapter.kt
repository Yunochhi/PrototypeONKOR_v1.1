package com.example.prototypeonkor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.Class.Appointment
import com.example.prototypeonkor.R
import java.time.LocalDate

class AppointmentAdapter(private val appointments: List<Appointment>) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val investigation: TextView = view.findViewById(R.id.investigationView)
        val date: TextView = view.findViewById(R.id.dateView)
        val time: TextView = view.findViewById(R.id.timeView)
        val doctorName: TextView = view.findViewById(R.id.doctorView)
        val status: TextView = view.findViewById(R.id.StatusView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.appointment_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val appoinment = appointments[position]
        with(holder)
        {
            investigation.text = appoinment.investigationName
            date.text = LocalDate.parse(appoinment.date).toString()
            time.text = appoinment.time
            doctorName.text = appoinment.doctorName
            status.text = appoinment.status.toString()
        }
    }
    override fun getItemCount() = appointments.size
}