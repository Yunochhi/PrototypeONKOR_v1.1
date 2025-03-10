package com.example.prototypeonkor.Activity

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
import com.example.prototypeonkor.APIService.Notification
import com.example.prototypeonkor.APIService.NotificationRequest
import com.example.prototypeonkor.APIService.SnilsRequest
import com.example.prototypeonkor.Class.HttpClient
import com.example.prototypeonkor.Fragments.*
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null
    private val CHANNEL_ID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        lifecycleScope.launch { pullNotifRec() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        replaceFragment(MainFragment(), R.id.fragment_container)

        // Обработка кликов кнопок
        binding.apply {
            actionBtn.setOnClickListener { startActivity(Intent(this@MainActivity, ProfileActivity::class.java)) }
            notificationsBtn.setOnClickListener { startActivity(Intent(this@MainActivity, NotificationActivity::class.java)) }
        }

        // Обработка кликов навигационного меню
        binding.bottomNavigationBar.setOnNavigationItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId)
            {
                R.id.navigation_protocols -> ProtocolsFragment()
                R.id.navigation_visits -> VisitsFragment()
                R.id.navigation_home -> MainFragment()
                R.id.navigation_dispensaryobservation -> DispancerFragment()
                else -> null
            }
            fragment?.let { replaceFragment(it, R.id.fragment_container) }
            fragment != null
        }
    }

    suspend fun pullNotifRec() {
        val snilsRequest = SnilsRequest("549 711 581 21")
        val protocols = withContext(Dispatchers.IO) { HttpClient.apiService.getProtocols(snilsRequest) }
        val notifList = withContext(Dispatchers.IO) { HttpClient.apiService.getNotifications(snilsRequest) }

        for (protocol in protocols) {
            val currentDate = LocalDate.now()
            val date = LocalDate.parse(protocol.info.date)
            val diff = ChronoUnit.DAYS.between(currentDate, date)

            if (diff in 1..2)
            {
                val notificationContent = "Лечащий врач: ${protocol.info.doctorName} \nДата: ${protocol.info.date} \nВремя: ${protocol.info.time}"
                val notification = Notification("Напоминание о визите!", notificationContent)
                val notificationRequest = NotificationRequest(snilsRequest.snils, notification)

                if (notifList.none { it.description == notificationContent })
                {
                    withContext(Dispatchers.IO) { HttpClient.apiService.addNotification(notificationRequest) }
                }

                sendNotification(notificationContent)
            }
        }
    }

    // Отправка уведомлений
    private fun sendNotification(content: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText(content)
            .setSmallIcon(R.drawable.onkor)
            .build()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    //Замена фрагментов меню
    private fun replaceFragment(fragment: Fragment, containerId: Int) {
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction().replace(containerId, fragment).commit()
        }
        currentFragment = fragment
    }
}
