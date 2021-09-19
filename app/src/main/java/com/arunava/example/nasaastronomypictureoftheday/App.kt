package com.arunava.example.nasaastronomypictureoftheday

import android.app.Application
import android.content.Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        App.appContext = applicationContext
    }

    companion object {

        @JvmStatic
        lateinit var appContext: Context
    }
}