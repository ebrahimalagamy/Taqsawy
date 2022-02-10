package com.hema.taqsawy.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hema.taqsawy.data.network.response.WeatherItem

class WeatherItemTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromWeatherItemList(value: MutableList<WeatherItem>): String {
            val gson = Gson()
            val type = object : TypeToken<MutableList<WeatherItem>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toWeatherItemList(value: String): MutableList<WeatherItem> {
            val gson = Gson()
            val type = object : TypeToken<MutableList<WeatherItem>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}