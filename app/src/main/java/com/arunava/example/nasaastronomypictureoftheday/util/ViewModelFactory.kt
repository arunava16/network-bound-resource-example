package com.arunava.example.nasaastronomypictureoftheday.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arunava.example.nasaastronomypictureoftheday.App
import com.arunava.example.nasaastronomypictureoftheday.model.local.LocalDatabase
import com.arunava.example.nasaastronomypictureoftheday.model.remote.ApiClient
import com.arunava.example.nasaastronomypictureoftheday.model.repository.Repository
import com.arunava.example.nasaastronomypictureoftheday.ui.MainViewModel

object ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            Repository(
                api = ApiClient,
                db = LocalDatabase.getInstance(App.appContext)
            )
        ) as T
    }
}