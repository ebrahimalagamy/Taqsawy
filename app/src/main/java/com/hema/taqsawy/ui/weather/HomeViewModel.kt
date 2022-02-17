package com.hema.taqsawy.ui.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hema.taqsawy.data.db.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.repository.Repository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var currentWeatherLiveData: LiveData<CurrentWeatherModel>
    private var state:MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var repository: Repository = Repository(application)
    fun setData(lat:String, long:String)
    {   repository.setinfo(lat,long)
        state.postValue(true)

    }
    fun getWeather(): LiveData<CurrentWeatherModel>? {
        if (::currentWeatherLiveData.isInitialized)
            return currentWeatherLiveData
        else
            return null
    }
    fun getState():MutableLiveData<Boolean>{
        return state
    }
    fun getData(){
        currentWeatherLiveData = repository.fetchData()

    }
    fun setState(boolean: Boolean){
        state.postValue(boolean)
    }




}