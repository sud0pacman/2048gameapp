package com.example.a2048.app

import android.app.Application
import com.example.a2048.data.MySharedPreferences
import com.example.a2048.domain.AppRepository

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        MySharedPreferences.init(this)
        AppRepository.getInstance(this)
    }
}