package com.arunava.example.nasaastronomypictureoftheday.util

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import retrofit2.Response

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result = MutableLiveData<Resource<ResultType>>()

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    suspend fun init() {
        val dbData = loadFromDb()
        result.postValue(Resource.Loading(dbData))
        if (shouldFetch(dbData)) {
            fetchFromNetwork(dbData)
        } else {
            result.postValue(Resource.Success(dbData))
        }
    }

    private suspend fun fetchFromNetwork(dbData: ResultType) {
        delay(3000)
        val apiResponse = networkFetch()
        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            saveFetchResult(apiResponse.body()!!)
            result.postValue(Resource.Success(loadFromDb()))
        } else {
            result.postValue(
                Resource.Error(
                    Throwable(apiResponse.errorBody()?.string()),
                    dbData
                )
            )
        }
    }

    // Called to create the API call.
    @WorkerThread
    protected abstract suspend fun networkFetch(): Response<RequestType>

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract suspend fun saveFetchResult(item: RequestType)

    // Called to get the cached data from the database.
    @WorkerThread
    protected abstract suspend fun loadFromDb(): ResultType

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    open fun shouldFetch(data: ResultType?) = true

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}
}