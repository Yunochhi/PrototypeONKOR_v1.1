package com.example.prototypeonkor.Activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prototypeonkor.Classes.PrefsHelper
import com.example.prototypeonkor.Objects.RetrofitInstance
import com.example.prototypeonkor.Classes.User
import com.example.prototypeonkor.databinding.ActivityProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import com.example.prototypeonkor.Classes.Requests.SnilsRequest
import com.example.prototypeonkor.Objects.SessionManager
import com.example.prototypeonkor.Enums.Gender

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var prefs: PrefsHelper
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        prefs = PrefsHelper(this)

        val snils = prefs.getSnilsString()

        lifecycleScope.launch {
            fillPatientInfo(snils)
        }

        binding.buttonExit.setOnClickListener {
            SessionManager.endSession(this)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private suspend fun fillPatientInfo(snils: String) {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }
        if (response.isSuccessful) {
            user = response.body()
            val fullName = user?.fullName ?: ""
            val nameParts = fullName.split(" ")
            withContext(Dispatchers.Main) {
                binding.userFullName.text = "${nameParts[0]}\n${nameParts[1][0]}. ${nameParts[2][0]}."
                binding.date.text = (user?.age ?: "").toString()
                binding.gender.text = when (user?.gender)
                {
                    Gender.MALE -> "Мужчина"
                    Gender.FEMALE -> "Женщина"
                    else -> ""
                }
                binding.hight.text = "${user?.height} см"
                binding.snils.text = (user?.snils ?: "").toString()
                binding.city.text = (user?.city ?: "").toString()
                binding.adress.text = (user?.address ?: "").toString()
                binding.phonenumber.text = (user?.phoneNumber ?: "").toString()
                binding.groupblood.text = (user?.bloodGroup ?: "").toString()
            }
        } else {
            response.errorBody()?.string()?.let { Log.d("errBody", it) }
        }
    }
}
