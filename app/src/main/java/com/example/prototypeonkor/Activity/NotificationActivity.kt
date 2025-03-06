package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototypeonkor.APIService.SnilsRequest
import com.example.prototypeonkor.Adapters.NotificationsAdapter
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.ActivityNotificationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            pullNotifRec()
        }
    }

    suspend fun pullNotifRec()
    {
        val notifList = mutableListOf<String>()
        val snilsRequest = SnilsRequest("549 711 581 21")
        val protocols = withContext(Dispatchers.IO)
        {
            RetrofitInstance.apiService.getProtocols(snilsRequest)
        }

        for (protocol in protocols)
        {
            val currentDate = LocalDate.now()
            val date = LocalDate.parse(protocol.info.date)
            val diff = ChronoUnit.DAYS.between(currentDate, date)
            if (diff in 1..5)
            {
                notifList.add("Уважаемый пользователь, на дату ${protocol.info.date} назначен поход к врачу. Лечащий врач ${protocol.info.doctorName}.")
            }
        }

        withContext(Dispatchers.Main) {
            binding.notifRec.layoutManager = LinearLayoutManager(this@NotificationActivity)
            val adapter = NotificationsAdapter(notifList)
            binding.notifRec.adapter = adapter
        }
    }
}