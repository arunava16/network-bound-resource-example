package com.arunava.example.nasaastronomypictureoftheday.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arunava.example.nasaastronomypictureoftheday.model.local.data.Picture

@Database(entities = [Picture::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun pictureOfTheDayDao(): PicturesDao
}