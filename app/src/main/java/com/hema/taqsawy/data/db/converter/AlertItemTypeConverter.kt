package com.hema.taqsawy.data.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hema.taqsawy.data.network.response.AlertsItem

class AlertItemTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromAlertItemList(value: MutableList<AlertsItem?>?): String? {
            val gson = Gson()
            val type = object : TypeToken<MutableList<AlertsItem>>() {}.type
            return gson.toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toAlertItemList(value: String?): MutableList<AlertsItem?>? {
            if (value == null) {
                return null
            }
            val gson = Gson()
            val type = object : TypeToken<MutableList<AlertsItem>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}