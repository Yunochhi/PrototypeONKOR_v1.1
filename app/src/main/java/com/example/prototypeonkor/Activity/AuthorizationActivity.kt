package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.prototypeonkor.databinding.ActivityAuthorizationBinding

//549 711 581 21 - 1 юзер


class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        binding.buttonFind.setOnClickListener {
            val snils = binding.editTextSNILS.text.toString()
            if (snils.isNotEmpty())
            {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("SNILS", snils)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this, "Введите корректный СНИЛС", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
