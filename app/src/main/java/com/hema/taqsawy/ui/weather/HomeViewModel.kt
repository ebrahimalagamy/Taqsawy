package com.hema.taqsawy.ui.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hema.taqsawy.data.db.pojo.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.repository.Repository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var currentWeatherLiveData: LiveData<CurrentWeatherModel>
    var repository: Repository = Repository(application)

    fun getWeather(): LiveData<CurrentWeatherModel> {
        currentWeatherLiveData = repository.fetchData()
        return currentWeatherLiveData
    }

}