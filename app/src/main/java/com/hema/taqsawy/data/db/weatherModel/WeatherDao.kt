package com.hema.taqsawy.data.db.weatherModel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeatherModel: CurrentWeatherModel?)

    @Query("SELECT * From CurrentWeatherModel WHERE lat=:lat AND lon=:lon")
    fun getAll(lat: String?, lon: String?): LiveData<CurrentWeatherModel>

    @Query("Delete from CurrentWeatherModel WHERE lat=:lat AND lon=:lng  ")
    suspend fun deleteAllWeather(lat: String, lng: String)

    @Query("Delete  from CurrentWeatherModel ")
    suspend fun deleteAll()

    @Query("SELECT * From CurrentWeatherModel WHERE lat=:lat AND lon=:lon")
    fun getAllForBroadCast(lat: String?, lon: String?): CurrentWeatherModel
}