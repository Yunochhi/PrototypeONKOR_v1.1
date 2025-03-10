package com.example.prototypeonkor.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototypeonkor.Adapters.ProtocolAdapter
import com.example.prototypeonkor.Class.HttpClient
import com.example.prototypeonkor.R
import com.example.prototypeonkor.APIService.SnilsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProtocolsFragment : Fragment(R.layout.fragment_protocols) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var protocolAdapter: ProtocolAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.protocolsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            fetchProtocols()
        }
    }

    private suspend fun fetchProtocols() {
        try {
            val snilsRequest = SnilsRequest("549 711 581 21")
            val protocols = withContext(Dispatchers.IO)
            {
                HttpClient.apiService.getProtocols(snilsRequest)
            }

            if (protocols.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {
                    protocolAdapter = ProtocolAdapter(protocols)
                    recyclerView.adapter = protocolAdapter
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