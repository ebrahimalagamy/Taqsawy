package com.hema.taqsawy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hema.taqsawy.data.db.alarmModel.AlarmDao
import com.hema.taqsawy.data.db.alarmModel.AlarmModel
import com.hema.taqsawy.data.db.favoritePlacesModel.FavoriteDao
import com.hema.taqsawy.data.db.favoritePlacesModel.FavoriteModel
import com.hema.taqsawy.data.db.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.db.weatherModel.WeatherDao

@Database(
    entities = [CurrentWeatherModel::class, FavoriteModel::class, AlarmModel::class],
    version = 1,
    exportSchema = false
)
abstract class ForecastDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var instance: ForecastDatabase? = null

        fun getInstance(context: Context): ForecastDatabase {
            return instance ?: synchronized(this) {
                instance ?: createDatabase(context).also { instance = it }
            }

        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, ForecastDatabase::class.java, "WeatherDataBase"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    }
}