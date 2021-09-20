package com.arunava.example.nasaastronomypictureoftheday.model.repository

import androidx.lifecycle.LiveData
import com.arunava.example.nasaastronomypictureoftheday.model.local.LocalDatabase
import com.arunava.example.nasaastronomypictureoftheday.model.local.data.Picture
import com.arunava.example.nasaastronomypictureoftheday.model.remote.Api
import com.arunava.example.nasaastronomypictureoftheday.model.remote.data.PictureOfTheDayResponse
import com.arunava.example.nasaastronomypictureoftheday.util.NetworkBoundResource
import com.arunava.example.nasaastronomypictureoftheday.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api,
    private val db: LocalDatabase
) {

    private val picturesDao by lazy { db.pictureOfTheDayDao() }

    suspend fun getPictureOfTheDay(dateString: String?): LiveData<Resource<Picture>> {
        return withContext(Dispatchers.IO) {
            object : NetworkBoundResource<Picture, PictureOfTheDayResponse>() {
                override suspend fun networkFetch(): Response<PictureOfTheDayResponse> {
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
            }.run {
                init()
                asLiveData()
            }
        }
    }

    suspend fun updateImagePath(imagePath: String) {
        picturesDao.updateImagePath(imagePath)
    }
}