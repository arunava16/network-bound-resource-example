package com.arunava.example.nasaastronomypictureoftheday.ui

import android.graphics.Bitmap
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arunava.example.nasaastronomypictureoftheday.model.local.data.Picture
import com.arunava.example.nasaastronomypictureoftheday.model.repository.Repository
import com.arunava.example.nasaastronomypictureoftheday.util.Resource
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainViewModel(private val repository: Repository) : ViewModel() {

//    val pictureOfTheDay = repository.getPictureOfTheDay()

    val pictureOfTheDay = MediatorLiveData<Resource<Picture>>()

    fun getPictureOfTheDay(date: String?) {
        pictureOfTheDay.addSource(repository.getPictureOfTheDay(date)) {
            pictureOfTheDay.value = it
        }
    }

    fun saveImage(bitmap: Bitmap, cacheDir: File) {
        viewModelScope.launch {
            val imageName = "apod_cached"

            val imageFile = File(cacheDir, imageName)
            val imagePath = imageFile.absolutePath
            try {
                val fos = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
                repository.updateImagePath(imagePath)
            } catch (e: Exception) {

            }
        }
    }
}