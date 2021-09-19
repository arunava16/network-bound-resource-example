package com.arunava.example.nasaastronomypictureoftheday.model.remote

import com.arunava.example.nasaastronomypictureoftheday.model.remote.data.PictureOfTheDayResponse

interface Api {

    val baseUrl: String;

    suspend fun getPictureOfTheDay(dateString: String?): PictureOfTheDayResponse
}