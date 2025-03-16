package com.example.prototypeonkor.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.Class.DispensaryObservation
import com.example.prototypeonkor.R

class DispensaryObservationMainAdapter(dispensary: List<DispensaryObservation>) : RecyclerView.Adapter<DispensaryObservationMainAdapter.ViewHolder>() {

    private val last = dispensary.take(1)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val LPU: TextView = view.findViewById(R.id.lpyTextView)
        val nextAppointmentDate: TextView = view.findViewById(R.id.nextdateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_dispensary_items, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val observation = last[position]
        with(holder)
        {
            LPU.text = "ЛПУ: ${observation.LPU}"
            nextAppointmentDate.text = "Следующая явка: ${observation.nextAppointmentDate}"
        }
    }


    override fun getItemCount() = last.size
}
