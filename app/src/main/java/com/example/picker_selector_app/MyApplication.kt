package com.example.picker_selector_app

import android.app.Application

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @Volatile private lateinit var instance: MyApplication

        fun getInstance() = instance
    }
}