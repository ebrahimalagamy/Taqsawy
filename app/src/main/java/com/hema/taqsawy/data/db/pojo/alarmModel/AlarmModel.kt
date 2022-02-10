package com.hema.taqsawy.data.db.pojo.alarmModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.hema.taqsawy.data.db.converter.AlarmItemTypeConverter

@Entity
@JvmSuppressWildcards
@TypeConverters(AlarmItemTypeConverter::class)
data class AlarmModel(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val weatherState: String,
    val accurateState: String,
    val daily: List<MaterialDayPicker.Weekday>,
    val billingTime: String,
    val userDescription: String,
    val minMaxChoice: String,
    val value: Double
)





