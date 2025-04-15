package com.example.prototypeonkor.Activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.prototypeonkor.Classes.Requests.SnilsRequest
import com.example.prototypeonkor.Objects.RetrofitInstance
import com.example.prototypeonkor.R

import com.example.prototypeonkor.databinding.ActivityDiseasesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityDiseases : AppCompatActivity() {

    private lateinit var binding: ActivityDiseasesBinding
    private lateinit var snils: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseasesBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        snils = intent.getStringExtra("SNILS") ?: ""

        lifecycleScope.launch {
            fetchDiseaseInfo()
        }

        val exitButton = findViewById<ImageButton>(R.id.backBtn)
        exitButton.setOnClickListener { finish() }

    }
    private suspend fun fetchDiseaseInfo() {
        try {
            val snilsRequest = SnilsRequest(snils)
            val dispensaries = withContext(Dispatchers.IO) {
                RetrofitInstance.apiService.getObservations(snilsRequest)
            }

            if (dispensaries.isNotEmpty()) {
                val diseaseName = dispensaries.firstOrNull()?.disease
                withContext(Dispatchers.Main) {
                    binding.textViewDiseaseName.text = "Название: $diseaseName"
                }
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("ActivityDiseases", "Ошибка при получении данных: ${e.message}")
            }
        }
    }
}

