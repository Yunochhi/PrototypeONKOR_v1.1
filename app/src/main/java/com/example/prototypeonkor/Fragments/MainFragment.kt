package com.example.prototypeonkor.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.Adapters.ProtocolsMainAdapter
import com.example.prototypeonkor.Objects.RetrofitInstance
import com.example.prototypeonkor.R
import com.example.prototypeonkor.Activity.ExaminationsActivity
import com.example.prototypeonkor.Adapters.AppointmentsMainAdapter
import com.example.prototypeonkor.Adapters.DispensaryObservationMainAdapter
import com.example.prototypeonkor.Classes.Requests.SnilsRequest
import com.example.prototypeonkor.databinding.FragmentMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var mainProtocolsRec: RecyclerView
    private lateinit var mainVisitsRec: RecyclerView
    private lateinit var mainDispensaryRec: RecyclerView
    private var snils: String = ""
    lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.inflate(layoutInflater)

        mainProtocolsRec = view.findViewById(R.id.mainProtocolsRec)
        mainVisitsRec = view.findViewById(R.id.mainVisitsRec)
        mainDispensaryRec = view.findViewById(R.id.mainDispensaryRec)

        view.findViewById<AppCompatImageButton>(R.id.buttonAllProtocols).setOnClickListener { replaceFragment(ProtocolsFragment()) }
        view.findViewById<AppCompatImageButton>(R.id.buttonAllVisits).setOnClickListener { replaceFragment(VisitsFragment()) }
        view.findViewById<AppCompatImageButton>(R.id.buttonAllDispancer).setOnClickListener { replaceFragment(DispancerFragment()) }

        view.findViewById<AppCompatImageButton>(R.id.genderObsBtn).setOnClickListener{
            val intent = Intent(view.context, ExaminationsActivity::class.java)
            startActivity(intent)}

        view.findViewById<AppCompatImageButton>(R.id.ageObsBtn).setOnClickListener{
            val intent = Intent(view.context, ExaminationsActivity::class.java)
            startActivity(intent)}

        view.findViewById<AppCompatImageButton>(R.id.sickObsBtn).setOnClickListener{
            val intent = Intent(view.context, ExaminationsActivity::class.java)
            startActivity(intent)}


        mainProtocolsRec.layoutManager = LinearLayoutManager(requireContext())
        mainVisitsRec.layoutManager = LinearLayoutManager(requireContext())
        mainDispensaryRec.layoutManager = LinearLayoutManager(requireContext())

        snils = arguments?.getString("SNILS").toString()

        viewLifecycleOwner.lifecycleScope.launch {
            fetchProtocols()
            fetchAppointments()
            fetchDispensary()
        }
    }

    private suspend fun fetchDispensary() {
        try
        {
            val snilsRequest = SnilsRequest(snils)
            val dispensary = withContext(Dispatchers.IO)
            {
                RetrofitInstance.apiService.getObservations(snilsRequest)
            }

            if (dispensary.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {
                    mainDispensaryRec.adapter = DispensaryObservationMainAdapter(dispensary)
                }
            }
        }
        catch (e: Exception)
        {
            withContext(Dispatchers.Main)
            {
                Log.e("Dispensary", "${e.message}")
            }
        }
    }

    private suspend fun fetchProtocols() {
        try
        {
            val snilsRequest = SnilsRequest(snils)
            val protocols = withContext(Dispatchers.IO)
            {
                RetrofitInstance.apiService.getProtocols(snilsRequest)
            }

            if (protocols.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {
                    mainProtocolsRec.adapter = ProtocolsMainAdapter(protocols)
                }
            }
        }
        catch (e: Exception)
        {
            withContext(Dispatchers.Main)
            {
                Log.e("Protocols", "${e.message}")
            }
        }
    }

    private suspend fun fetchAppointments() {
        try
        {
            val snilsRequest = SnilsRequest(snils)
            val appointments = withContext(Dispatchers.IO)
            {
                RetrofitInstance.apiService.getAppointments(snilsRequest)
            }

            if (appointments.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {
                    mainVisitsRec.adapter = AppointmentsMainAdapter(appointments)
                }
            }
        }
        catch (e: Exception)
        {
            withContext(Dispatchers.Main)
            {
                Log.e("Appointments", "${e.message}")
            }
        }
    }

    private fun replaceFragment(fragment: Fragment)
    {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
        updateBottomNavigation(fragment)
    }

    private fun updateBottomNavigation(fragment: Fragment)
    {
        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationBar)
        when (fragment)
        {
            is ProtocolsFragment -> bottomNavigationView.selectedItemId = R.id.navigation_protocols
            is VisitsFragment -> bottomNavigationView.selectedItemId = R.id.navigation_visits
            is DispancerFragment -> bottomNavigationView.selectedItemId = R.id.navigation_dispensaryobservation
        }
    }
}
