package com.example.prototypeonkor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.Classes.DispensaryObservation
import com.example.prototypeonkor.R

class DispensaryAdapter(private val dispensaries: List<DispensaryObservation>) : RecyclerView.Adapter<DispensaryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val lpu: TextView = view.findViewById(R.id.LPUView)
        val nextAppointmentDate: TextView = view.findViewById(R.id.nextAppointmentDateView)
        val doctorName: TextView = view.findViewById(R.id.doctorNameView)
        val disease: TextView = view.findViewById(R.id.diseaseView)
        val mkbCodes: TextView = view.findViewById(R.id.mkbCodesView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dispensary_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val dispensary = dispensaries[position]
        with(holder)
        {
            lpu.text = dispensary.LPU
            nextAppointmentDate.text = dispensary.nextAppointmentDate
            doctorName.text = dispensary.doctorName
            disease.text = dispensary.disease
            mkbCodes.text = dispensary.mkbCodes
        }
    }
    override fun getItemCount() = dispensaries.size
}