package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.prototypeonkor.APIService.SnilsRequest
import com.example.prototypeonkor.Class.PrefsHelper
import com.example.prototypeonkor.Class.RetrofitInstance
import com.example.prototypeonkor.Class.User
import com.example.prototypeonkor.databinding.ActivityAuthorizationBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

//549 711 581 21 - 1 юзер

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding
    private lateinit var prefs: PrefsHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = PrefsHelper(this)
        if (prefs.getSnilsString().isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.buttonFind.setOnClickListener {
            if (binding.editTextSNILS.text.isNullOrEmpty()) {
                Snackbar.make(binding.root, "Введите СНИЛС ❗", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val snils = binding.editTextSNILS.text.toString().trim()
            val splitedSnils = snils.split(" ").toTypedArray()

            if (splitedSnils.size != 4 || splitedSnils[0].length != 3 || splitedSnils[1].length != 3
                || splitedSnils[2].length != 3 || splitedSnils[3].length != 2
            ) {
                Snackbar.make(binding.root, "Введите корректный СНИЛС ❗", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val response = getResponce(snils)
                Log.d("DASDASDA", response.message())

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        prefs.saveSnilsString(snils)
                        val intent = Intent(this@AuthorizationActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Пользователь не найден ❗",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    suspend fun getResponce(snils: String): Response<User> {
        val snilsRequest = SnilsRequest(snils)
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.apiService.getUserInfo(snilsRequest)
        }

        return response

    }
}
