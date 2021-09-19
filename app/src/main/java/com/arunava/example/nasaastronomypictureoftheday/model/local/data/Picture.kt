package com.arunava.example.nasaastronomypictureoftheday.model.local.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture")
data class Picture(
    @PrimaryKey
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String,
    val cachedImagePath:String? = null
)