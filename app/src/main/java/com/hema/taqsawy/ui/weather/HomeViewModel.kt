package com.hema.taqsawy.ui.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hema.taqsawy.data.db.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.repository.Repository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var currentWeatherLiveData: LiveData<CurrentWeatherModel>
    var repository: Repository = Repository(application)

    fun setData(lat: String, long: String, lang: String, unit_: String) {
        repository.setinfo(lat, long, lang, unit_)
        currentWeatherLiveData = repository.fetchData()
    }

    fun getWeather(): LiveData<CurrentWeatherModel>? {
        if (::currentWeatherLiveData.isInitialized) {
            return currentWeatherLiveData
        } else
            return null
    }


}