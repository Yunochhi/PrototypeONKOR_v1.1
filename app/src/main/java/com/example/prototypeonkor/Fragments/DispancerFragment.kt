package com.example.prototypeonkor.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.APIService.SnilsRequest
import com.example.prototypeonkor.Adapters.DispensaryAdapter
import com.example.prototypeonkor.Adapters.ProtocolAdapter
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DispancerFragment : Fragment(R.layout.fragment_dispancer) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dispensaryAdapter: DispensaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.DispancerRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            fetchDispensaries()
        }
    }

    private suspend fun fetchDispensaries() {
        try {
            val snilsRequest = SnilsRequest("549 711 581 21")
            val dispensaries = withContext(Dispatchers.IO) {
                RetrofitInstance.apiService.getObservations(snilsRequest)
            }

            if (dispensaries.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {
                    dispensaryAdapter = DispensaryAdapter(dispensaries)
                    recyclerView.adapter = dispensaryAdapter
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