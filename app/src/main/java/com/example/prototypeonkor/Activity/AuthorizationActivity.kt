package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.prototypeonkor.databinding.ActivityAuthorizationBinding

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)



        binding.buttonFind.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
