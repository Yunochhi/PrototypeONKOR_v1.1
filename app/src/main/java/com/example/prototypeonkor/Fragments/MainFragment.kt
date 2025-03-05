package com.example.prototypeonkor.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.Adapters.ProtocolsMainAdapter
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.R
import com.example.prototypeonkor.APIService.SnilsRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment(R.layout.fragment_main) {

    lateinit var mainProtocolsRec: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAllProtocols: AppCompatImageButton = view.findViewById(R.id.buttonAllProtocols)
        val buttonAllVisits: AppCompatImageButton = view.findViewById(R.id.buttonAllVisits)
        val buttonAllDispancer: AppCompatImageButton = view.findViewById(R.id.buttonAllDispancer)

        mainProtocolsRec = view.findViewById(R.id.mainProtocolsRec)
        mainProtocolsRec.layoutManager = LinearLayoutManager(requireContext())

        buttonAllProtocols.setOnClickListener {
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, ProtocolsFragment())
            transaction.addToBackStack(null)
            transaction.commit()
            Protocols()
        }

        buttonAllVisits.setOnClickListener {
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, VisitsFragment())
            transaction.addToBackStack(null)
            transaction.commit()
            Visits()
        }

        buttonAllDispancer.setOnClickListener {
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, DispancerFragment())
            transaction.addToBackStack(null)
            transaction.commit()
            Dispancer()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            fetchProtocols()
        }
    }

    private suspend fun fetchProtocols() {
        try {
            val snilsRequest = SnilsRequest("549 711 581 21")
            val protocols = withContext(Dispatchers.IO)
            {
                RetrofitInstance.apiService.getProtocols(snilsRequest)
            }
            if (protocols.isNotEmpty()) {
                withContext(Dispatchers.Main)
                {
                    val adapter = ProtocolsMainAdapter(protocols)
                    mainProtocolsRec.adapter = adapter
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

    private fun Dispancer() {
        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationBar)
        bottomNavigationView.selectedItemId = R.id.navigation_dispensaryobservation
    }

    private fun Visits() {
        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationBar)
        bottomNavigationView.selectedItemId = R.id.navigation_visits
    }

    private fun Protocols() {
        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationBar)
        bottomNavigationView.selectedItemId = R.id.navigation_protocols
    }

}
