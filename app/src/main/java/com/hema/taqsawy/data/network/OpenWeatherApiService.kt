package com.hema.taqsawy.data.network

import com.hema.taqsawy.data.db.pojo.weatherModel.CurrentWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


// https://api.openweathermap.org/data/2.5/onecall?lat=55.755825&lon=37.617298&units=metric&exclude=minutely&appid=d777ae60141a13cd6e1dd200ac6c5fdb
interface OpenWeatherApiService {

    @GET("onecall")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") appid: String
    ): Response<CurrentWeatherModel>

}