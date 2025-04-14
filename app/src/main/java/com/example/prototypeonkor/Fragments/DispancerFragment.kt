package com.example.prototypeonkor.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.Adapters.DispensaryAdapter
import com.example.prototypeonkor.Classes.Requests.SnilsRequest
import com.example.prototypeonkor.Objects.RetrofitInstance
import com.example.prototypeonkor.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DispancerFragment : Fragment(R.layout.fragment_dispancer) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dispensaryAdapter: DispensaryAdapter
    private var snils: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.DispancerRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        snils = arguments?.getString("SNILS").toString()

        viewLifecycleOwner.lifecycleScope.launch {
            fetchDispensaries()
        }
    }

    private suspend fun fetchDispensaries() {
        try {
            val snilsRequest = SnilsRequest(snils)
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
                Log.e("DispensariesFragment", "${e.message}")
            }
        }
    }
}