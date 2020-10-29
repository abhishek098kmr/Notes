package com.app.notes

import android.app.Application
import android.preference.PreferenceManager

class MyApplication : Application() {
    companion object {
        private lateinit var sInstance: MyApplication

        @Synchronized
        fun getInstance(): MyApplication? {
            return sInstance
        }

    }


    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }
}