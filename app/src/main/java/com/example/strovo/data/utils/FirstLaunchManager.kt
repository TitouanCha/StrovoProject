package com.example.strovo.data.utils

import android.content.Context
import androidx.core.content.edit

class FirstLaunchManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean("is_first_launch", true)
    }

    fun setFirstLaunch(isFirst: Boolean) {
        prefs.edit() {
            putBoolean("is_first_launch", isFirst)
        }
    }
}