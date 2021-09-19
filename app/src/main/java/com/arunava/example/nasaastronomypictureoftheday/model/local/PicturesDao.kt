package com.arunava.example.nasaastronomypictureoftheday.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arunava.example.nasaastronomypictureoftheday.model.local.data.Picture

@Dao
interface PicturesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPicture(picture: Picture)

    @Query("SELECT * FROM picture LIMIT 1")
    suspend fun getPicture(): Picture

    @Query("UPDATE picture SET cachedImagePath = :imagePath")
    suspend fun updateImagePath(imagePath: String)
}