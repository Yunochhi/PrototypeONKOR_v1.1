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

        binding.editTextSNILS.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?)
            {
                s?.let {
                    val digitsOnly = it.toString().replace("\\D".toRegex(), "")
                    if (digitsOnly.length > 11)
                    {
                        binding.editTextSNILS.setText(digitsOnly.substring(0, 11))
                        binding.editTextSNILS.setSelection(11)
                        return
                    }
                    binding.editTextSNILS.removeTextChangedListener(this)
                    binding.editTextSNILS.setText(SNILSFormat(digitsOnly))
                    binding.editTextSNILS.setSelection(binding.editTextSNILS.text?.length ?: 0)
                    binding.editTextSNILS.addTextChangedListener(this)
                }
            }
        })

        binding.buttonFind.setOnClickListener {
            val rawSNILS = binding.editTextSNILS.text.toString().replace("\\D".toRegex(), "")
            if (rawSNILS.length == 11)
            {
                startActivity(Intent(this, MainActivity::class.java))
            }
            else
            {
                Toast.makeText(this, "Введите корректный СНИЛС", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun SNILSFormat(snils: String): String
    {
        return snils.chunked(3).joinToString(" ").trim()
    }}
