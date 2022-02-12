package com.hema.taqsawy.data.db.weatherModel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.hema.taqsawy.data.db.converter.AlertItemTypeConverter
import com.hema.taqsawy.data.db.converter.DailyItemTypeConverter
import com.hema.taqsawy.data.db.converter.HourlyItemTypeConverter
import com.hema.taqsawy.data.network.response.AlertsItem
import com.hema.taqsawy.data.network.response.Current
import com.hema.taqsawy.data.network.response.DailyItem
import com.hema.taqsawy.data.network.response.HourlyItem
import java.io.Serializable

@Entity(primaryKeys = ["lon", "lat"])
@JvmSuppressWildcards
@TypeConverters(
    AlertItemTypeConverter::class,
    DailyItemTypeConverter::class,
    HourlyItemTypeConverter::class
)

data class CurrentWeatherModel(

    @field:SerializedName("alerts")
    val alerts: List<AlertsItem?>? = null,

    @field:SerializedName("current")
    @Embedded(prefix = "current_")
    val current: Current? = null,

    @field:SerializedName("timezone")
    val timezone: String? = null,

    @field:SerializedName("timezone_offset")
    val timezoneOffset: Int? = null,

    @field:SerializedName("daily")
    val daily: List<DailyItem?>? = null,

    @field:SerializedName("lon")
    val lon: Double,

    @field:SerializedName("hourly")
    val hourly: List<HourlyItem?>? = null,

    @field:SerializedName("lat")
    val lat: Double
) : Serializable