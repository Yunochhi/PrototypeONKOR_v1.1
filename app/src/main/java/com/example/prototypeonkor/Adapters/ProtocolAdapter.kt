package com.example.prototypeonkor.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.prototypeonkor.APIService.ProtocolFile
import com.example.prototypeonkor.APIService.ProtocolRequest
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File

class ProtocolAdapter(private val protocols: List<ProtocolFile>, private val context: Context) : RecyclerView.Adapter<ProtocolAdapter.ProtocolViewHolder>() {

    inner class ProtocolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val fileTextView: TextView = itemView.findViewById(R.id.fileTextView)
        val investigationView: TextView = itemView.findViewById(R.id.investigationView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProtocolViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.protocol_info, parent, false)
        return ProtocolViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProtocolViewHolder, position: Int) {
        val protocol = protocols[position]
        with(holder) {
            dateTextView.text = protocol.info.date
            timeTextView.text = protocol.info.time
            titleTextView.text = protocol.info.lpu
            investigationView.text = protocol.info.investigationName
            fileTextView.text = protocol.fileName
            fileTextView.setOnClickListener { downloadProtocol(protocol.fileName) }
        }
    }

    override fun getItemCount() = protocols.size

    private fun downloadProtocol(fileName: String) {
        val snils = "549 711 581 21"
        val protocolRequest = ProtocolRequest(snils, fileName)

        CoroutineScope(Dispatchers.IO).launch {
            try
            {

                val response: ResponseBody = RetrofitInstance.apiService.openProtocols(protocolRequest)
                val file = File(context.getExternalFilesDir(null), fileName)
                file.outputStream().use { fileOutputStream -> response.byteStream().use { inputStream -> inputStream.copyTo(fileOutputStream) } }
                openfFile(file)

            }
            catch (e: Exception)
            {
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(context, "Ошибка открытия файла", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openfFile(file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.startActivity(intent)
    }
}