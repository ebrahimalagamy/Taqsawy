package com.hema.taqsawy.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hema.taqsawy.data.db.pojo.alarmModel.AlarmDao
import com.hema.taqsawy.data.db.pojo.alarmModel.AlarmModel
import com.hema.taqsawy.data.db.pojo.favoritePlacesModel.FavoriteDao
import com.hema.taqsawy.data.db.pojo.favoritePlacesModel.FavoriteModel
import com.hema.taqsawy.data.db.pojo.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.db.pojo.weatherModel.WeatherDao

@Database(
    entities = [CurrentWeatherModel::class, FavoriteModel::class, AlarmModel::class],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun alarmDao(): AlarmDao


}