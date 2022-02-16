package com.hema.taqsawy.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hema.taqsawy.alarm.NotificationNormal
import com.hema.taqsawy.data.alarmModel.AlarmModel
import com.hema.taqsawy.internal.Constants.Companion.ALARM_KEY
import com.hema.taqsawy.providers.SharedPreferencesProvider
import com.hema.taqsawy.alarm.alarm.ringtone
import java.lang.reflect.Type
import java.util.ArrayList


class alarmClass(): BroadcastReceiver() {
    lateinit var mPrefs: SharedPreferencesProvider
    override fun onReceive(context: Context?, p1: Intent?) {
        mPrefs = SharedPreferencesProvider(context!!)



        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show()
        val bundle = p1?.extras
        if (bundle != null) {
            val cancel: String = bundle?.getString("cancel")!!
            val id: String = bundle.getString("id")!!
            val type: String = bundle.getString("type")!!

            if (type.equals("alarm")) {
                val notification = NotificationAlarm(context!!, id!!)

                Log.e("alarm class", id.toString())
                if (cancel != "null" && cancel.equals("cancel")) {
                    ringtone.stop()
                    cancel_alarm(context, id.toInt())
                    notification.cancelNotification()
                } else {

                    notification.notifyBtn()

                }
            } else if (type.equals("notification")) {
                if (cancel != "null" && cancel.equals("cancel")) {
                    cancel_alarm(context,id.toInt())
            }else{
                    val notification = NotificationNormal(context!!, "weather cast",id)
                    notification.notifyBtn()
            }}
        }
    }
    private fun cancel_alarm(context: Context?,id:Int?) {
        val alarmManager: AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context,alarmClass::class.java)
        val pedningIntent = PendingIntent.getBroadcast(context,id!!,intent, 0)
        alarmManager.cancel(pedningIntent)
        val list = mPrefs.getModelList(ALARM_KEY)
        list?.removeAt(id)
        mPrefs.setList(ALARM_KEY,list)
    }


}