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

            override fun shouldFetch(data: Picture?): Boolean {
                return true
            }
        }.asLiveData()
    }

//    fun getPictureOfTheDay() = networkBoundResource(
//        query = {
//            picturesDao.getPicture()
//        },
//        fetch = {
//            api.getPictureOfTheDay()
//        },
//        saveFetchResult = {
//
//        }
//    )

    private fun <ResultType, RequestType> networkBoundResource(
        query: () -> Flow<ResultType>,
        fetch: suspend () -> RequestType,
        saveFetchResult: suspend (RequestType) -> Unit,
        shouldFetch: (ResultType) -> Boolean = { true }
    ) = flow {
        val data = query().first()

        val flow = if (shouldFetch(data)) {
            emit(Resource.Loading(data))

            try {
                saveFetchResult(fetch())
                query().map { Resource.Success(it) }
            } catch (e: Exception) {
                query().map { Resource.Error(e, it) }
            }
        } else {
            query().map { Resource.Success(it) }
        }

        emitAll(flow)
    }

    suspend fun updateImagePath(imagePath: String) {
        picturesDao.updateImagePath(imagePath)
    }
}