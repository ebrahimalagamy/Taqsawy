package com.hema.taqsawy.ui.favorite.favoriteDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hema.taqsawy.data.db.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.repository.Repository

class FavoriteDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository(application)

    fun getFavoriteWeatherData(lat2: String?, lng2: String?): LiveData<CurrentWeatherModel> {
        return repository.fetchDataForFavorite(lat2, lng2)
    }

}