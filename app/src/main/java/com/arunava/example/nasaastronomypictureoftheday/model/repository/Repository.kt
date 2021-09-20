package com.arunava.example.nasaastronomypictureoftheday.model.repository

import androidx.lifecycle.LiveData
import com.arunava.example.nasaastronomypictureoftheday.model.local.LocalDatabase
import com.arunava.example.nasaastronomypictureoftheday.model.local.data.Picture
import com.arunava.example.nasaastronomypictureoftheday.model.remote.Api
import com.arunava.example.nasaastronomypictureoftheday.model.remote.data.PictureOfTheDayResponse
import com.arunava.example.nasaastronomypictureoftheday.util.NetworkBoundResource
import com.arunava.example.nasaastronomypictureoftheday.util.Resource
import kotlinx.coroutines.flow.*

class Repository(
    private val api: Api,
    private val db: LocalDatabase
) {

    private val picturesDao by lazy { db.pictureOfTheDayDao() }

    fun getPictureOfTheDay(dateString: String?): LiveData<Resource<Picture>> {
        return object : NetworkBoundResource<Picture, PictureOfTheDayResponse>() {
            override suspend fun networkFetch(): PictureOfTheDayResponse {
                return api.getPictureOfTheDay(dateString)
            }

            override suspend fun saveFetchResult(item: PictureOfTheDayResponse) {
                picturesDao.insertPicture(item.run {
                    Picture(
                        date = date,
                        explanation = explanation,
                        hdUrl = hdUrl,
                        mediaType = mediaType,
                        serviceVersion = serviceVersion,
                        title = title,
                        url = url
                    )
                })
            }

            override suspend fun loadFromDb(): Picture {
                return picturesDao.getPicture()
            }
        }.asLiveData()
    }

    suspend fun updateImagePath(imagePath: String) {
        picturesDao.updateImagePath(imagePath)
    }
}