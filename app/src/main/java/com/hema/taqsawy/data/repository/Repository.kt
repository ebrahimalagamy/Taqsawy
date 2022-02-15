package com.hema.taqsawy.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.hema.taqsawy.R
import com.hema.taqsawy.data.db.ForecastDatabase
import com.hema.taqsawy.data.db.favoritePlacesModel.FavoriteModel
import com.hema.taqsawy.data.db.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.network.RetrofitInstance
import com.hema.taqsawy.internal.Constants.Companion.API_KEY
import com.hema.taqsawy.internal.UnitSystem
import com.hema.taqsawy.providers.SharedPreferencesProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Repository(private val application: Application) {

    private val localWeatherDB = ForecastDatabase.getInstance(application).weatherDao()
    private val localFavoriteDB = ForecastDatabase.getInstance(application).favoriteDao()

    private var sharedPref: SharedPreferencesProvider = SharedPreferencesProvider(application)
    private val latLong = sharedPref.latLong
    private val latPref = latLong[0].toString()
    private val lngPref = latLong[1].toString()
    private val language = sharedPref.getLanguage.toString()
    private val unit = sharedPref.getUnit.toString()

    fun fetchData(): LiveData<CurrentWeatherModel> {
        val exceptionHandlerException = CoroutineExceptionHandler { _, _: Throwable ->

        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            if (unit == "imperial") {
                UnitSystem.tempUnit = application.getString(R.string.Feherinhite)
                UnitSystem.WindSpeedUnit = application.getString(R.string.mileshr)
            } else if (unit == "metric") {
                UnitSystem.tempUnit = application.getString(R.string.celicious)
                UnitSystem.WindSpeedUnit = application.getString(R.string.mpers)
            }
            val response = RetrofitInstance.getWeatherService().getCurrentWeather(
                latPref, lngPref, "minutely", unit, language, API_KEY
            )
            Log.e("dsdasd",latPref+"kkk"+lngPref)

            if (response.isSuccessful) {
                Log.e("Repository","success")
                localWeatherDB.insert(response.body())
            } else {
                Log.e("Repository","failure of api call "+response.message())

            }
        }
        return localWeatherDB.getAll(latPref, lngPref)
    }

    fun fetchDataForFavorite(favLat: String?, favLng: String?): LiveData<CurrentWeatherModel> {

        val exceptionHandlerException = CoroutineExceptionHandler { _, _: Throwable ->
        }

        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            val response = RetrofitInstance.getWeatherService()
                .getCurrentWeather(favLat, favLng, "minutely", unit, language, API_KEY)
            if (response.isSuccessful) {
                localWeatherDB.insert(response.body())
            }
        }
        return localWeatherDB.getAll(favLat, favLng)
    }

    fun insertFavoritePlaces(favoriteModel: FavoriteModel) {
        localFavoriteDB.insertFavoritePlaces(favoriteModel)
    }

    fun fetchFavoritePlaces(): LiveData<List<FavoriteModel>> {
        return localFavoriteDB.getFavoritePlaces()
    }

    fun deleteFromDb(lat: String, lng: String) {
        CoroutineScope(Dispatchers.IO).launch {
            localFavoriteDB.deleteAll(lat, lng)
            localWeatherDB.deleteAllWeather(lat, lng)
        }

    }

}



