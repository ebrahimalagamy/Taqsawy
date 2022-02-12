package com.hema.taqsawy.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hema.taqsawy.data.db.favoritePlacesModel.FavoriteModel
import com.hema.taqsawy.data.db.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.repository.Repository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application)

    private val navigate = MutableLiveData<List<String>>()


    fun getWeatherFromFavorite(lat2: String?, lng2: String?): LiveData<CurrentWeatherModel> {
        return repository.fetchDataForFavorite(lat2, lng2)
    }

    fun insertFavoriteToDataBase(lat3: String?, lng3: String?) {
        repository.fetchDataForFavorite(lat3, lng3)
    }


    fun insertFavorite(favoriteModel: FavoriteModel) {
        repository.insertFavoritePlaces(favoriteModel)
    }

    fun fetchFavorite(): LiveData<List<FavoriteModel>> {
        return repository.retriveFavoritePlaces()
    }

    fun deleteItem(lat: String, lng: String) {
        repository.deleteFromDb(lat, lng)
    }



    fun getNavigation(): MutableLiveData<List<String>> {
        return navigate
    }

    fun onClick(lat: String, lng: String) {
        val latLng = listOf<String>(lat, lng)
        navigate.value = latLng
    }


}