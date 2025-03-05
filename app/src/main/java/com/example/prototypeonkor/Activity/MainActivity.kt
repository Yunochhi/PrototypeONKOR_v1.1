package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.prototypeonkor.Fragments.DispancerFragment
import com.example.prototypeonkor.Fragments.MainFragment
import com.example.prototypeonkor.Fragments.ProtocolsFragment
import com.example.prototypeonkor.Fragments.VisitsFragment
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

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


