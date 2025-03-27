package com.example.prototypeonkor.Activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.prototypeonkor.APIService.*
import com.example.prototypeonkor.Class.Notification
import com.example.prototypeonkor.Class.PrefsHelper
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.Fragments.*
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val CHANNEL_ID = "ONKOR"
    private var snils = ""
    private lateinit var prefs: PrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        createNotificationChannel()

        prefs = PrefsHelper(this)
        snils = prefs.getSnilsString()

        lifecycleScope.launch {
            pullNotifRec(snils)
        }

        binding.actionBtn.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }

        binding.notificationsBtn.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_protocols -> replaceFragment(ProtocolsFragment())
                R.id.navigation_visits -> replaceFragment(VisitsFragment())
                R.id.navigation_home -> replaceFragment(MainFragment())
                R.id.navigation_dispensaryobservation -> replaceFragment(DispancerFragment())
            }
            true
        }
        replaceFragment(MainFragment())
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
            Snackbar.make(binding.root, "${e.message}", Snackbar.LENGTH_SHORT).show()
            Log.e("MainActivity", "${e.message}")
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

    private fun replaceFragment(fragment: Fragment) {

        val bundle = Bundle()
        bundle.putString("SNILS", snils)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}
