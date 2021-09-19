package com.arunava.example.nasaastronomypictureoftheday.model.remote

import com.arunava.example.nasaastronomypictureoftheday.model.remote.data.PictureOfTheDayResponse
import java.net.HttpURLConnection
import java.net.URL

object ApiClient : Api {

    override val baseUrl = "https://api.nasa.gov/planetary/apod"

    override suspend fun getPictureOfTheDay(dateString: String?): PictureOfTheDayResponse {
        val endpoint = StringBuilder(baseUrl)
            .append("?api_key=DEMO_KEY")
        dateString?.let {
            endpoint.append("&date=$dateString")
        }
        val url = URL(endpoint.toString())
        val urlConnection = url.openConnection() as HttpURLConnection

        val jsonString = urlConnection.inputStream.bufferedReader().readText()

        return PictureOfTheDayResponse.fromJsonString(jsonString)
    }
}