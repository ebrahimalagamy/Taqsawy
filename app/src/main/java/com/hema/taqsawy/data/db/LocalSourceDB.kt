package com.hema.taqsawy.data.db

import android.content.Context
import androidx.room.Room

object LocalSourceDB {
    fun getInstance(context: Context): ForecastDatabase {
        return Room.databaseBuilder(
            context, ForecastDatabase::class.java, "WeatherDataBase"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    }
}