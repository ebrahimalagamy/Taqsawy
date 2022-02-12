package com.hema.taqsawy.data.network

import com.hema.taqsawy.data.db.weatherModel.CurrentWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/** https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&
lang=zh_cn&units=metric&appid=1bd3261cd147a7b08c1d6f8c52319e0e */

interface ApiWeather {

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