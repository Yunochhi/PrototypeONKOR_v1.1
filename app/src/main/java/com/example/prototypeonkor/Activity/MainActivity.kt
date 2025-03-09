package com.example.prototypeonkor.Activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.prototypeonkor.APIService.Notification
import com.example.prototypeonkor.APIService.NotificationRequest
import com.example.prototypeonkor.APIService.SnilsRequest
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.Fragments.DispancerFragment
import com.example.prototypeonkor.Fragments.MainFragment
import com.example.prototypeonkor.Fragments.ProtocolsFragment
import com.example.prototypeonkor.Fragments.VisitsFragment
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null
    private val CHANNEL_ID = "my_channel_id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        createNotificationChannel()

        lifecycleScope.launch {
            pullNotifRec()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        replaceFragment(MainFragment(), R.id.fragment_container)

        binding.actionBtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.notificationsBtn.setOnClickListener{
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.bottomNavigationBar.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_protocols -> {
                    if (currentFragment !is ProtocolsFragment) {
                        replaceFragment(ProtocolsFragment(), R.id.fragment_container)
                    }
                    true
                }
                R.id.navigation_visits -> {
                    if (currentFragment !is VisitsFragment) {
                        replaceFragment(VisitsFragment(), R.id.fragment_container)
                    }
                    true
                }
                R.id.navigation_home -> {
                    if (currentFragment !is MainFragment) {
                        replaceFragment(MainFragment(), R.id.fragment_container)
                    }
                    true
                }
                R.id.navigation_dispensaryobservation -> {
                    if (currentFragment !is DispancerFragment) {
                        replaceFragment(DispancerFragment(), R.id.fragment_container)
                    }
                    true
                }
                else -> false
            }
        }
    }

    suspend fun pullNotifRec() {
        val snilsRequest = SnilsRequest("549 711 581 21")
        val protocols = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getProtocols(snilsRequest)
        }
        val notifList = withContext(Dispatchers.IO)
        {
            RetrofitInstance.apiService.getNotifications(snilsRequest)
        }
        for (protocol in protocols) {
            val currentDate = LocalDate.now()
            val date = LocalDate.parse(protocol.info.date)
            val diff = ChronoUnit.DAYS.between(currentDate, date)

            val notificationContent = "Лечащий врач: ${protocol.info.doctorName} \n" + "Дата: ${protocol.info.date} \n" + "Время: ${protocol.info.time}"
            val notification = Notification("Напоминание о визите!", notificationContent)
            val notificationRequest = NotificationRequest(snilsRequest.snils, notification)

            if (notifList.contains(notification))
            {
                return
            }

            if (diff in 1..2) {
                withContext(Dispatchers.IO)
                {
                    RetrofitInstance.apiService.addNotification(notificationRequest)
                }
            }

            sendNotification(notificationContent)
        }
    }

    private fun sendNotification(content: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("Напоминание о визите!").setContentText(content).setSmallIcon(R.drawable.onkor).build()
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
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

    private fun replaceFragment(fragment: Fragment, containerId: Int) {
        currentFragment?.let {
            supportFragmentManager.beginTransaction()
                .hide(it)
                .commit()
        }
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commit()
        }
        currentFragment = fragment
    }
}


