package com.example.prototypeonkor.Classes

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.prototypeonkor.Objects.SessionManager

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupLifecycleObserver()
    }

    private fun setupLifecycleObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onStop(owner: LifecycleOwner) {
                    SessionManager.updateLastActivityTime(this@MyApp)
                }

                override fun onStart(owner: LifecycleOwner) {
                    SessionManager.startTimer(this@MyApp)
                }
            }
        )
    }
}