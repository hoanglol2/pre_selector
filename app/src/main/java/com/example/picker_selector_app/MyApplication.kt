package com.example.picker_selector_app

import android.app.Application
import timber.log.Timber

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        @Volatile private lateinit var instance: MyApplication

        fun getInstance() = instance
    }
}