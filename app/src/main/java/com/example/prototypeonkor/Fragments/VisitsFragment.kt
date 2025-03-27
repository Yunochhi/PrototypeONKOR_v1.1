package com.example.prototypeonkor.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.APIService.SnilsRequest
import com.example.prototypeonkor.Adapters.AppointmentAdapter
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VisitsFragment : Fragment(R.layout.fragment_visits) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentAdapter: AppointmentAdapter
    private var snils: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.AppointmentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        snils = arguments?.getString("SNILS") ?: ""

        viewLifecycleOwner.lifecycleScope.launch {
            fetchProtocols()
        }
    }

    private suspend fun fetchProtocols() {
        try {
            val snilsRequest = SnilsRequest(snils)
            val appointments = withContext(Dispatchers.IO)
            {
                RetrofitInstance.apiService.getAppointments(snilsRequest)
            }

            if (appointments.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {
                    appointmentAdapter = AppointmentAdapter(appointments)
                    recyclerView.adapter = appointmentAdapter
                }
            }
        }
        catch (e: Exception)
        {
            withContext(Dispatchers.Main)
            {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("ProtocolsFragment", "${e.message}")
            }
        }
    }
}