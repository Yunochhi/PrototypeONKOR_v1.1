package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prototypeonkor.Adapters.NotificationsAdapter
import com.example.prototypeonkor.Classes.PrefsHelper
import com.example.prototypeonkor.Classes.Requests.SnilsRequest
import com.example.prototypeonkor.Objects.RetrofitInstance
import com.example.prototypeonkor.databinding.ActivityNotificationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var prefs: PrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = PrefsHelper(this)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val snils = prefs.getSnilsString()

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
