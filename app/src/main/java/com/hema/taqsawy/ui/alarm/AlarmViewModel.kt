package com.hema.taqsawy.ui.alarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hema.taqsawy.data.db.pojo.alarmModel.AlarmModel
import com.hema.taqsawy.data.repository.Repository

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository(application)

    //recyclerView Data handling
    fun insertAlarmToDB(alarmModel: AlarmModel) {
        repository.insertAlarm(alarmModel)
    }

    fun fetchAlarmItems(): LiveData<List<AlarmModel>> {
        return repository.retrieveAlarmData()
    }

    fun deleteAlarmItem(id: Int) {
        repository.deleteAlarm(id)
    }
}