package com.example.prototypeonkor.Activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prototypeonkor.APIService.PatientApiService
import com.example.prototypeonkor.Objects.RetrofitInstance
import com.example.prototypeonkor.Classes.Requests.StudiesListRequest
import com.example.prototypeonkor.Classes.Requests.SnilsRequest
import com.example.prototypeonkor.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import com.example.prototypeonkor.Enums.Gender

class ExaminationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_examinations)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textViewStudies = findViewById<TextView>(R.id.textViewStudies)
        val snils = intent.getStringExtra("SNILS")

        if (snils != null) {
            getUserInfoAndFetchStudies(snils, textViewStudies)
        }

        val exitButton = findViewById<ImageButton>(R.id.backBtn)
        exitButton.setOnClickListener { finish() }

    }

    private fun getUserInfoAndFetchStudies(snils: String, textViewStudies: TextView) {
        val snilsRequest = SnilsRequest(snils)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.apiService.getUserInfo(snilsRequest)

                if (response.isSuccessful) {
                    val user = response.body()
                    val age = user?.age ?: 0
                    val gender = when (user?.gender) {
                        Gender.MALE -> "MALE"
                        Gender.FEMALE -> "FEMALE"
                        else -> ""
                    }
                    val studiesListRequest = StudiesListRequest(age, gender)

                    val studiesResponse = RetrofitInstance.apiService.getStudiesList(studiesListRequest)

                    if (studiesResponse.isSuccessful) {
                        val studies = studiesResponse.body()?.get("studies") ?: emptyList()
                        withContext(Dispatchers.Main) {
                            textViewStudies.text = studies.joinToString("\n")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("ExaminationsActivity", "Исключение: ${e.message}", e)
            }
        }
    }
}