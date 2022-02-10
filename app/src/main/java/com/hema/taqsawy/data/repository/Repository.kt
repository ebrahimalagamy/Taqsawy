package com.hema.taqsawy.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.hema.taqsawy.R
import com.hema.taqsawy.data.db.LocalSourceDB
import com.hema.taqsawy.data.db.pojo.alarmModel.AlarmModel
import com.hema.taqsawy.data.db.pojo.favoritePlacesModel.FavoriteModel
import com.hema.taqsawy.data.db.pojo.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.network.WeatherClient
import com.hema.taqsawy.internal.UnitSystem
import com.hema.taqsawy.providers.SharedPreferencesProvider
import kotlinx.coroutines.*

class Repository(private val application: Application) {
    private val API_KEY = "d777ae60141a13cd6e1dd200ac6c5fdb"
    private val localWeatherDB = LocalSourceDB.getInstance(application).weatherDao()
    private val localFavoriteDB = LocalSourceDB.getInstance(application).favoriteDao()
    private val localAlarmDB = LocalSourceDB.getInstance(application).alarmDao()

    private var sharedPref: SharedPreferencesProvider = SharedPreferencesProvider(application)
    private val latLong = sharedPref.latLong

    private val latPref = latLong[0].toString()
    private val lngPref = latLong[1].toString()
    private val language = sharedPref.getLanguage.toString()
    private val unit = sharedPref.getUnit.toString()


    // weather ----------------------------------------------------------------------------------------------------------------

    fun fetchData(): LiveData<CurrentWeatherModel> {
        val exceptionHandlerException = CoroutineExceptionHandler { _, t: Throwable ->

        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            if (latPref != null) {
                if (unit == "imperial") {
                    UnitSystem.tempUnit = application.getString(R.string.Feherinhite)
                    UnitSystem.WindSpeedUnit = application.getString(R.string.mileshr)
                } else if (unit == "metric") {
                    UnitSystem.tempUnit = application.getString(R.string.celicious)
                    UnitSystem.WindSpeedUnit = application.getString(R.string.mpers)
                }
                val response = WeatherClient.getWeatherService().getCurrentWeather(
                    latPref, lngPref, "minutely", unit, language, API_KEY
                )
                Log.d("responseeeeeeee", latPref + unit + "--------" + lngPref)

                if (response.isSuccessful) {
                    // localWeatherDB.deleteAll()
                    localWeatherDB.insert(response.body())
                    Log.d("responseeeeeeee", response.body().toString())
                } else {
                    Log.d("responseeeeeeee", response.message())
                }
            }
        }

        Log.d("responseeeeeeee", "ffffffffffffffffffffff")
        return localWeatherDB.getAll(latPref, lngPref)
    }


    // favorite --------------------------------------------------------------------------------------------------------------

    fun fetchDataForFavorite(favLat: String?, favLng: String?): LiveData<CurrentWeatherModel> {

        val exceptionHandlerException = CoroutineExceptionHandler { _, t: Throwable ->
        }

        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            val response = WeatherClient.getWeatherService()
                .getCurrentWeather(favLat, favLng, "minutely", unit, language, API_KEY)
            Log.d("responseeeeeeee", favLat + "--------" + favLng)
            if (response.isSuccessful) {
                localWeatherDB.insert(response.body())
                Log.d("responseeeeeeee", response.body().toString())
            } else {
                Log.d("responseeeeeeee", response.message())
            }
        }

        Log.d("responseeeeeeee", "ffffffffffffffffffffff")
        return localWeatherDB.getAll(favLat, favLng)
    }


    fun insertFavoritePlaces(favoriteModel: FavoriteModel) {
        localFavoriteDB.insertFavoritePlaces(favoriteModel)
    }


    fun retriveFavoritePlaces(): LiveData<List<FavoriteModel>> {
        return localFavoriteDB.getFavoritePlaces()
    }


    fun deleteFromDb(lat: String, lng: String) {
        CoroutineScope(Dispatchers.IO).launch {
            localFavoriteDB.deleteAll(lat, lng)
            localWeatherDB.deleteAllWeather(lat, lng)
        }

    }


    /// alarm --------------------------------------------------------------------------------------------------
    fun insertAlarm(alarmModel: AlarmModel) {
        localAlarmDB.insertAlarmData(alarmModel)
    }

    fun retrieveAlarmData(): LiveData<List<AlarmModel>> {
        return localAlarmDB.getAlarmData()
    }

    fun deleteAlarm(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            localAlarmDB.deleteAlarm(id)
        }

    }

    /// BCR----------------------------------------------------------------------------------------------

    fun fetchDataForBCR() {
        runBlocking(Dispatchers.IO) {
            launch {
                try {
                    if (latPref != null) {
                        if (unit == "imperial") {
                            UnitSystem.tempUnit = application.getString(R.string.Feherinhite)
                            UnitSystem.WindSpeedUnit = application.getString(R.string.mileshr)
                        } else if (unit == "metric") {
                            UnitSystem.tempUnit = application.getString(R.string.celicious)
                            UnitSystem.WindSpeedUnit = application.getString(R.string.mpers)
                        }
                        val response = WeatherClient.getWeatherService()
                            .getCurrentWeather(
                                latPref,
                                lngPref,
                                "minutely",
                                unit,
                                language,
                                API_KEY
                            )
                        if (response.isSuccessful) {
                            localWeatherDB.insert(response.body())
                        }
                    }
                } catch (e: Exception) {
                    Log.i("error", e.message.toString())
                }
            }
        }

    }


}



