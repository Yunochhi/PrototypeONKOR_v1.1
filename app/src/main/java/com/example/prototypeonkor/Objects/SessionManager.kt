package com.example.prototypeonkor.Objects

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.content.edit
import com.example.prototypeonkor.Activity.AuthorizationActivity
import com.example.prototypeonkor.Classes.PrefsHelper

object SessionManager {
    private const val SESSION_TIMEOUT = 15 * 60 * 1000L // 15 минут
    private const val PREFS_NAME = "session_prefs"
    private const val KEY_LAST_ACTIVITY_TIME = "last_activity_time"
    private var sessionTimerHandler: Handler? = null
    private var sessionTimeoutRunnable: Runnable? = null

    fun initSessionTimer(context: Context) {
        sessionTimerHandler = Handler(Looper.getMainLooper())
        sessionTimeoutRunnable = Runnable {
            endSession(context)
        }
        startTimer(context)
    }
    fun isSessionExpired(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lastActivityTime = prefs.getLong(KEY_LAST_ACTIVITY_TIME, 0L)

        if (lastActivityTime == 0L) return true

        val currentTime = System.currentTimeMillis()

        return (currentTime - lastActivityTime) > SESSION_TIMEOUT
    }

    fun startTimer(context: Context) {
        sessionTimerHandler?.removeCallbacks(sessionTimeoutRunnable!!)
        sessionTimerHandler?.postDelayed(
            sessionTimeoutRunnable!!,
            SESSION_TIMEOUT
        )
        updateLastActivityTime(context)
    }

    fun updateLastActivityTime(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putLong(KEY_LAST_ACTIVITY_TIME, System.currentTimeMillis())
            apply()
        }
    }

    fun endSession(context: Context) {
        sessionTimerHandler?.removeCallbacks(sessionTimeoutRunnable!!)
        clearSessionData(context)
        redirectToLogin(context)
    }

    private fun clearSessionData(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            clear()
            apply()
        }
    }

    private fun redirectToLogin(context: Context) {
        val intent = Intent(context, AuthorizationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val snilsPrefs = PrefsHelper(context)
        snilsPrefs.saveSnilsString("")
        context.startActivity(intent)
        (context as? Activity)?.finishAffinity()
    }
}