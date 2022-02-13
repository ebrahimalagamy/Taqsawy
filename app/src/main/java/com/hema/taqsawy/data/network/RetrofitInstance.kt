package com.hema.taqsawy.data.network

import com.hema.taqsawy.internal.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    fun getWeatherService(): ApiWeather {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiWeather::class.java)
    }
}