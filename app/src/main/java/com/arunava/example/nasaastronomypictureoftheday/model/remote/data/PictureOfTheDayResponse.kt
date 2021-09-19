package com.arunava.example.nasaastronomypictureoftheday.model.remote.data

import org.json.JSONObject

data class PictureOfTheDayResponse(
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
) {
    companion object {

        @JvmStatic
        fun fromJsonString(jsonString: String) = JSONObject(jsonString).run {
            PictureOfTheDayResponse(
                date = optString("date"),
                explanation = optString("explanation"),
                hdUrl = optString("hdurl"),
                mediaType = optString("media_type"),
                serviceVersion = optString("service_version"),
                title = optString("title"),
                url = optString("url")
            )
        }
    }
}