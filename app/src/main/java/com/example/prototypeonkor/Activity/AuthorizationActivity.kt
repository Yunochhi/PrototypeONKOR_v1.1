package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prototypeonkor.Class.PrefsHelper
import com.example.prototypeonkor.databinding.ActivityAuthorizationBinding
import com.google.android.material.snackbar.Snackbar

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
        if (prefs.getSnilsString().isNotEmpty())
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.buttonFind.setOnClickListener {
            if (binding.editTextSNILS.text.isNullOrEmpty())
            {
                Snackbar.make(binding.root, "Введите СНИЛС", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val snils = binding.editTextSNILS.text.toString().trim()
            val splitedSnils = snils.split(" ").toTypedArray()
            if (splitedSnils.size != 4 || splitedSnils[0].length != 3 || splitedSnils[1].length != 3
                || splitedSnils[2].length != 3 || splitedSnils[3].length != 2)
            {
                Snackbar.make(binding.root, "Введите корректный СНИЛС", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, MainActivity::class.java)
            prefs.saveSnilsString(snils)
            startActivity(intent)
        }
    }

}
