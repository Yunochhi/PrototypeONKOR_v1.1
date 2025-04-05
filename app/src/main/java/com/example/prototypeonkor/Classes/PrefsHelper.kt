package com.example.prototypeonkor.Classes

import android.content.Context
import android.content.SharedPreferences

class PrefsHelper(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "onkor_prefs"
        private const val KEY_GLOBAL_STRING = "snils_string"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSnilsString(value: String) {
        sharedPref.edit()
            .putString(KEY_GLOBAL_STRING, value)
            .apply()
    }

    fun getSnilsString(): String {
        return sharedPref.getString(KEY_GLOBAL_STRING, "default_value").toString()
    }
}