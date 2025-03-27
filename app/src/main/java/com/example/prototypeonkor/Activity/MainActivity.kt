package com.example.prototypeonkor.Activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.prototypeonkor.APIService.*
import com.example.prototypeonkor.Class.Notification
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.Fragments.*
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val CHANNEL_ID = "ONKOR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        createNotificationChannel()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val snils = intent.getStringExtra("SNILS") ?: ""

        lifecycleScope.launch {
            pullNotifRec(snils)
        }

        binding.actionBtn.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
        binding.notificationsBtn.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("SNILS", snils)
            startActivity(intent)
        }

        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_protocols -> replaceFragment(ProtocolsFragment(), snils)
                R.id.navigation_visits -> replaceFragment(VisitsFragment(), snils)
                R.id.navigation_home -> replaceFragment(MainFragment(), snils)
                R.id.navigation_dispensaryobservation -> replaceFragment(DispancerFragment(), snils)
            }
            true
        }
        replaceFragment(MainFragment(), snils)
    }

    private suspend fun pullNotifRec(snils: String)
    {
        try
        {
            val snilsRequest = SnilsRequest(snils)
            val appointments = withContext(Dispatchers.IO) {
                RetrofitInstance.apiService.getAppointments(snilsRequest)
            }
            val existingNotifs = withContext(Dispatchers.IO) {
                RetrofitInstance.apiService.getNotifications(snilsRequest)
            }

            appointments.filter { ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(it.date)) in 1..2 } .forEach { protocol -> val description = "Лечащий врач: ${protocol.doctorName}\nДата: ${protocol.date}\nВремя: ${protocol.time}"
                    if (existingNotifs.none { it.description == description })
                    {
                        withContext(Dispatchers.IO)
                        {
                            RetrofitInstance.apiService.addNotification(Notification(snilsRequest.snils, "Напоминание о визите!", description))
                        }
                    }
                    sendNotification(description)
                }
        }
        catch(e:Exception)
        {
           //Тут тип что-то должно быть, но не будет)
        }
    }

    private fun sendNotification(content: String)
    {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Напоминание о визите").setContentText(content).setSmallIcon(R.drawable.onkor).build()
        getSystemService(NotificationManager::class.java)?.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel()
    {
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT))
    }

    private fun replaceFragment(fragment: Fragment, snils: String) {

        val bundle = Bundle()
        bundle.putString("SNILS", snils)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}
