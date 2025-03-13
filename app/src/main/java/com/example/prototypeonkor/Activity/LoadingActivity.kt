package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        simulateLoading()
    }

    private fun simulateLoading()
    {
        binding.progressBar.visibility = View.VISIBLE
        Handler().postDelayed({ binding.progressBar.visibility = View.GONE
        navigateToAuthorization()
        }, 3000)
    }

    private fun navigateToAuthorization()
    {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
