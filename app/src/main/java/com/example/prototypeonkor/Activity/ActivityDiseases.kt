package com.example.prototypeonkor.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.prototypeonkor.Classes.Disease
import com.example.prototypeonkor.Classes.DispensaryObservation
import com.example.prototypeonkor.Classes.Requests.MkbCodeRequest
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

    @SuppressLint("SetTextI18n")
    private fun displayDiseases(diseases: List<DispensaryObservation>) {
        val inflater = LayoutInflater.from(this)
        val container = binding.diseasesContainer

        container.removeAllViews()

        for (disease in diseases) {
            val diseaseView = inflater.inflate(R.layout.disease_item, container, false)

            disease.diseaseDetails?.let { detail ->
                diseaseView.findViewById<TextView>(R.id.textViewMkbCode).text = "МКБ: ${detail.mkbCodes}"
                diseaseView.findViewById<TextView>(R.id.textViewDiseaseName).text = "Название: ${disease.disease}"
                diseaseView.findViewById<TextView>(R.id.textViewFrequencyVisits).text = "Частота посещений: ${detail.frequencyVisits}"
                diseaseView.findViewById<TextView>(R.id.textViewControlledIndicators).text = "Контролируемые показатели: ${detail.controlledIndicators}"
                diseaseView.findViewById<TextView>(R.id.textViewDurationObservation).text = "Длительность наблюдения: ${detail.durationObservation}"
                diseaseView.findViewById<TextView>(R.id.textViewNote).text = "Примечание: ${detail.note}"
            }

            container.addView(diseaseView)
        }
    }

    private suspend fun fetchDiseaseInfo() {
        try {
            val snilsRequest = SnilsRequest(snils)
            val dispensaries = withContext(Dispatchers.IO) {
                RetrofitInstance.apiService.getObservations(snilsRequest)
            }

            val dispensaryWithDetails = dispensaries.map { dispensary ->
                val diseaseDetails = getDiseaseDetails(dispensary.mkbCodes)
                dispensary.copy(diseaseDetails = diseaseDetails)
            }

            if (dispensaryWithDetails.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    displayDiseases(dispensaryWithDetails)
                }
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("ActivityDiseases", "Ошибка при получении данных: ${e.message}")
            }
        }
    }

    private suspend fun getDiseaseDetails(mkbCode: String): Disease? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.apiService.getDiseaseByMkbCode(MkbCodeRequest(mkbCode))
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("ActivityDiseases", "Ошибка при получении данных о болезни: ${response.message()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("ActivityDiseases", "Ошибка при запросе болезни: ${e.message}")
                null
            }
        }
    }
}
