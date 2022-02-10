package com.hema.taqsawy.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hema.taqsawy.data.network.response.DailyItem

class DailyItemTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromDailyItemList(value: MutableList<DailyItem>): String {
            val gson = Gson()
            val type = object : TypeToken<MutableList<DailyItem>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toDailyItemList(value: String): MutableList<DailyItem> {
            val gson = Gson()
            val type = object : TypeToken<MutableList<DailyItem>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}