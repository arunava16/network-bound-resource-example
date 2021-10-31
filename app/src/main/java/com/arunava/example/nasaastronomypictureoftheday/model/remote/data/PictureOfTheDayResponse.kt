package com.arunava.example.nasaastronomypictureoftheday.model.remote.data

import com.google.gson.annotations.SerializedName

data class PictureOfTheDayResponse(
    @SerializedName("date") val date: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("hdurl") val hdUrl: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("service_version") val serviceVersion: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?
)