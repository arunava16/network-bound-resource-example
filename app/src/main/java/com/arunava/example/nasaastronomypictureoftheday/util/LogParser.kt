package com.arunava.example.nasaastronomypictureoftheday.util

import android.util.Log
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject

object LogParser : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        when {
            message.startsWith("{") -> {
                val jsonString = JSONObject(message).toString(4)
                logLengthy("OkHttp", jsonString)
            }
            message.startsWith("[") -> {
                val jsonString = JSONArray(message).toString(4)
                logLengthy("OkHttp", jsonString)
            }
            else -> {
                Platform.get().log(message)
            }
        }
    }

    private tailrec fun logLengthy(tag: String, message: String) {
        if (message.length <= 4000) {
            Log.d(tag, message)
        } else {
            Log.d(tag, message.substring(0, 4000))
            logLengthy(tag, message.substring(4000))
        }
    }
}