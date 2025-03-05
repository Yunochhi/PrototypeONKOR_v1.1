package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prototypeonkor.R
import com.example.prototypeonkor.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoadingBinding

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

        simulateLoading(binding.progressBar)
    }

    private fun simulateLoading(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        Thread {
            Thread.sleep(3000)
            runOnUiThread {
                progressBar.visibility = View.GONE
                navigateToSecondActivity()
            }
        }.start()
    }

    private fun navigateToSecondActivity() {
        val intent = Intent(this, AuthorizationActivity::class.java)
        startActivity(intent)
        finish()
    }
}
