package com.hema.taqsawy.data.db.alarmModel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarmData(alarmModel: AlarmModel)

    @Query("SELECT * From AlarmModel")
    fun getAlarmData(): LiveData<List<AlarmModel>>

    @Query("Delete from AlarmModel WHERE id=:id")
    suspend fun deleteAlarm(id: Int)

    @Query("SELECT * From AlarmModel WHERE id=:id")
    fun getAlarmById(id: Int): AlarmModel
}