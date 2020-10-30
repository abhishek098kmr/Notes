package com.app.notes.utils

import android.content.Context
import androidx.core.content.edit
import com.app.notes.MyApplication

object PreferenceUtil {
    private val sharedPreferences =
        MyApplication.getInstance()
            ?.getSharedPreferences(Constants.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun putAny(key: String, value: Any) {
        when (value) {
            is String -> sharedPreferences?.edit { putString(key, value) }
            is Boolean -> sharedPreferences?.edit { putBoolean(key, value) }
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences?.getString(key, defaultValue)!!
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences?.getBoolean(key, defaultValue)!!
    }


    fun clear() {
        sharedPreferences?.edit { clear() }
    }
}