package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototypeonkor.APIService.SnilsRequest
import com.example.prototypeonkor.Adapters.NotificationsAdapter
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.databinding.ActivityNotificationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val snils = intent.getStringExtra("SNILS") ?: ""


        lifecycleScope.launch {
            pullNotifRec(snils)
        }
    }

    private suspend fun pullNotifRec(snils: String)
    {
        val snilsRequest = SnilsRequest(snils)
        val notifList = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getNotifications(snilsRequest)
        }

        withContext(Dispatchers.Main)
        {
            binding.notifRec.layoutManager = LinearLayoutManager(this@NotificationActivity)
            binding.notifRec.adapter = NotificationsAdapter(notifList)
        }
    }
}
