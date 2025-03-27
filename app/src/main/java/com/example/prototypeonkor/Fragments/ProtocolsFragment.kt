package com.example.prototypeonkor.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototypeonkor.APIService.ProtocolRequest
import com.example.prototypeonkor.Adapters.ProtocolAdapter
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.R
import com.example.prototypeonkor.APIService.SnilsRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File

class ProtocolsFragment : Fragment(R.layout.fragment_protocols) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var protocolAdapter: ProtocolAdapter
    private var snils: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.protocolsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        snils = arguments?.getString("SNILS").toString()

        viewLifecycleOwner.lifecycleScope.launch {
            fetchProtocols()
        }
    }

    private suspend fun fetchProtocols() {
        try {
            val snilsRequest = SnilsRequest(snils)
            val protocols = withContext(Dispatchers.IO) {
                RetrofitInstance.apiService.getProtocols(snilsRequest)
            }

            if (protocols.isNotEmpty())
            {
                withContext(Dispatchers.Main)
                {
                    protocolAdapter = ProtocolAdapter(protocols, requireContext()){fileName -> downloadProtocol(fileName)}
                    recyclerView.adapter = protocolAdapter
                }
            }
            return
        }
        catch (e: Exception)
        {
            withContext(Dispatchers.Main)
            {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("ProtocolsFragment", "${e.message}")
            }
        }
        return
    }

    private  fun downloadProtocol(fileName: String) {
        val protocolRequest = ProtocolRequest(snils, fileName)

        CoroutineScope(Dispatchers.IO).launch {
            try
            {
                val response: ResponseBody = RetrofitInstance.apiService.openProtocols(protocolRequest)
                val file = File(requireContext().getExternalFilesDir(null), fileName)
                file.outputStream().use { fileOutputStream -> response.byteStream().use { inputStream -> inputStream.copyTo(fileOutputStream) }}
                openFile(file)
            }
            catch (e: Exception)
            {
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(requireContext(), "Ошибка открытия файла", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openFile(file: File) {
        val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(intent)
    }

}