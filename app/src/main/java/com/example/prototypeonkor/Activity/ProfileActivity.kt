package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prototypeonkor.Class.PrefsHelper
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.Class.User
import com.example.prototypeonkor.APIService.SnilsRequest
import com.example.prototypeonkor.databinding.ActivityProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope

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
            fillFullname(snils)
            Age(snils)
            Height(snils)
            Snils(snils)
            City(snils)
            Address(snils)
            PhoneNumber(snils)
            BloodGroup(snils)
        }

        binding.buttonExit.setOnClickListener {
            prefs.saveSnilsString("")
            startActivity(Intent(this, AuthorizationActivity::class.java))
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private suspend fun fillFullname(snils: String) {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }
        if (response.isSuccessful) {
            user = response.body()
            val fullName = user?.fullName ?: ""
            val nameParts = fullName.split(" ")
            val formattedName = nameParts.joinToString("\n")
            withContext(Dispatchers.Main) {
                binding.userFullName.text = formattedName
            }
        } else {
            response.errorBody()?.string()?.let { Log.d("errBody", it) }
        }
    }

    private suspend fun Age(snils: String) {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }
        if (response.isSuccessful) {
            user = response.body()
            binding.date.text = (user?.age ?: "").toString()
        } else {
            response.errorBody()?.string()?.let { Log.d("errBody", it) }
        }
    }

    private suspend fun Height (snils: String) {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }
        if (response.isSuccessful) {
            user = response.body()
            binding.ves.text = ((user?.height ?: ("" + "кг"))).toString()
        } else {
            response.errorBody()?.string()?.let { Log.d("errBody", it) }
        }
    }

    private suspend fun Snils (snils: String) {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }
        if (response.isSuccessful) {
            user = response.body()
            binding.insurancepolicy.text = (user?.snils ?: "").toString()
        } else {
            response.errorBody()?.string()?.let { Log.d("errBody", it) }
        }
    }

    private suspend fun City (snils: String) {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }
        if (response.isSuccessful) {
            user = response.body()
            binding.town.text = (user?.city ?: "").toString()
        } else {
            response.errorBody()?.string()?.let { Log.d("errBody", it) }
        }
    }

    private suspend fun Address (snils: String) {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }
        if (response.isSuccessful) {
            user = response.body()
            binding.street.text = (user?.address ?: "").toString()
        } else {
            response.errorBody()?.string()?.let { Log.d("errBody", it) }
        }
    }

    private suspend fun PhoneNumber (snils: String) {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }
        if (response.isSuccessful) {
            user = response.body()
            binding.phonenumber.text = (user?.phoneNumber ?: "").toString()
        } else {
            response.errorBody()?.string()?.let { Log.d("errBody", it) }
        }
    }

    private suspend fun BloodGroup (snils: String) {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }
        if (response.isSuccessful) {
            user = response.body()
            binding.groupblood.text = (user?.bloodGroup ?: "").toString()
        } else {
            response.errorBody()?.string()?.let { Log.d("errBody", it) }
        }
    }


}
