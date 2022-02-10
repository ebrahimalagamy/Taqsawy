package com.hema.taqsawy.data.db

import android.app.Application
import androidx.room.Room

object LocalSourceDB {
    fun getInstance(application: Application): DataBase {
        return Room.databaseBuilder(
            application,
            DataBase::class.java,
            "WeatherDataBase"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    }
}