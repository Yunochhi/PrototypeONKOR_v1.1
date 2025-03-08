package com.example.prototypeonkor.Activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.example.prototypeonkor.APIService.SnilsRequest
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.ActivityAuthorizationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class AuthorizationActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthorizationBinding
    lateinit var buttonFind : AppCompatButton
    private val CHANNEL_ID = "my_channel_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        createNotificationChannel()

        lifecycleScope.launch {
            pullNotifRec()
        }

        binding.buttonFind.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MyChannel"
            val description = "Channel for my notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    suspend fun pullNotifRec() {
        val snilsRequest = SnilsRequest("549 711 581 21")
        val protocols = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getProtocols(snilsRequest)
        }

        for (protocol in protocols) {
            val currentDate = LocalDate.now()
            val date = LocalDate.parse(protocol.info.date)
            //val diff = ChronoUnit.DAYS.between(currentDate, date)

            //if (diff in 1..5) {
            val notificationContent = "Лечащий врач: ${protocol.info.doctorName} \n" + "Дата: ${protocol.info.date} \n" + "Время: ${protocol.info.time}"
            sendNotification(notificationContent)
            //}
        }
    }

    private fun sendNotification(content: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Напоминание о визите!").setContentText(content).setSmallIcon(R.drawable.onkor).build()
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
