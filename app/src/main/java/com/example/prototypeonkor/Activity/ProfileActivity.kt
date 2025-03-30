package com.example.prototypeonkor.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prototypeonkor.Class.PrefsHelper
import com.example.prototypeonkor.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var prefs: PrefsHelper

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

        binding.buttonExit.setOnClickListener {
            prefs.saveSnilsString("")
            startActivity(Intent(this, AuthorizationActivity::class.java))
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
