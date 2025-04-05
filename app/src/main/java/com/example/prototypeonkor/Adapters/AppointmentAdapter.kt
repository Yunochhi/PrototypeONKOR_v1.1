package com.example.prototypeonkor.Adapters

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.Classes.Appointment
import com.example.prototypeonkor.Enums.AppointmentStatus
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
        val appointment = appointments[position]
        with(holder)
        {
            investigation.text = appointment.investigationName
            date.text = LocalDate.parse(appointment.date).toString()
            time.text = appointment.time
            doctorName.text = appointment.doctorName
            status.text = when (appointment.status)
            {
                AppointmentStatus.COMPLETED -> Html.fromHtml("Прошёл <font color=\"#4CAF50\">✔</font>")
                AppointmentStatus.MISSED -> Html.fromHtml("Не прошёл <font color=\"#F44336\">❌</font>")
                AppointmentStatus.PLANNED -> Html.fromHtml("В ожидании <font color=\"#2196F3\">🕒</font>")
            }
        }
    }
    override fun getItemCount() = appointments.size
}
