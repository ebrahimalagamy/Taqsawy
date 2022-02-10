package com.hema.taqsawy.data.db.converter

import androidx.room.TypeConverter
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmItemTypeConverter {


    companion object {
        @TypeConverter
        @JvmStatic
        fun fromAlarmItemList(value: MutableList<MaterialDayPicker.Weekday>): String {
            val gson = Gson()
            val type = object : TypeToken<MutableList<MaterialDayPicker.Weekday>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toAlarmItemList(value: String): MutableList<MaterialDayPicker.Weekday> {
            val gson = Gson()
            val type = object : TypeToken<MutableList<MaterialDayPicker.Weekday>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}