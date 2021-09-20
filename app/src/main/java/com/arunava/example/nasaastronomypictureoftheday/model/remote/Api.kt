package com.arunava.example.nasaastronomypictureoftheday.model.remote

import com.arunava.example.nasaastronomypictureoftheday.model.remote.data.PictureOfTheDayResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    companion object {
        const val BASE_URL = "https://api.nasa.gov/planetary/"
    }

    @GET("apod")
    suspend fun getPictureOfTheDay(
        @Query("date") date: String?,
        @Query("api_key") apiKey: String = "DEMO_KEY"
    ): Response<PictureOfTheDayResponse>
}