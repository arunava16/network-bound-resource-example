package com.arunava.example.nasaastronomypictureoftheday.model.repository

import androidx.lifecycle.LiveData
import com.arunava.example.nasaastronomypictureoftheday.model.local.LocalDatabase
import com.arunava.example.nasaastronomypictureoftheday.model.local.data.Picture
import com.arunava.example.nasaastronomypictureoftheday.model.remote.Api
import com.arunava.example.nasaastronomypictureoftheday.model.remote.data.PictureOfTheDayResponse
import com.arunava.example.nasaastronomypictureoftheday.util.NetworkBoundResource
import com.arunava.example.nasaastronomypictureoftheday.util.Resource
import kotlinx.coroutines.CoroutineScope
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api,
    private val db: LocalDatabase
) {

    private val picturesDao by lazy { db.pictureOfTheDayDao() }

    fun getPictureOfTheDay(
        scope: CoroutineScope,
        dateString: String?
    ): LiveData<Resource<Picture>> {
        return object : NetworkBoundResource<Picture, PictureOfTheDayResponse>(scope) {
            override suspend fun networkFetch(): Response<PictureOfTheDayResponse> {
                return api.getPictureOfTheDay(dateString)
            }

            override suspend fun saveFetchResult(item: PictureOfTheDayResponse) {
                if (item.date != null) {
                    picturesDao.deletePictures()
                    picturesDao.insertPicture(item.run {
                        Picture(
                            date = date!!,
                            explanation = explanation.orEmpty(),
                            hdUrl = hdUrl.orEmpty(),
                            mediaType = mediaType.orEmpty(),
                            serviceVersion = serviceVersion.orEmpty(),
                            title = title.orEmpty(),
                            url = if (mediaType == "image") url.orEmpty() else thumbnailUrl.orEmpty()
                        )
                    })
                }
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