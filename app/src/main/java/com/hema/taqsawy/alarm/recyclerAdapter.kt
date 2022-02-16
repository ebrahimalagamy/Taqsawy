package com.hema.taqsawy.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hema.taqsawy.data.alarmModel.AlarmModel
import com.hema.taqsawy.databinding.ItemAlarmBinding
import com.hema.taqsawy.alarm.alarm.isringtoneInit
import com.hema.taqsawy.alarm.alarm.ringtone
import com.hema.taqsawy.internal.Constants
import com.hema.taqsawy.providers.SharedPreferencesProvider

class recyclerAdapter(val context:Context,val list:ArrayList<AlarmModel>):
    RecyclerView.Adapter<recyclerAdapter.VH>() {
    inner class VH(view:View):RecyclerView.ViewHolder(view){

    }
     val mPrefs:SharedPreferencesProvider =SharedPreferencesProvider(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(
            ItemAlarmBinding.inflate(LayoutInflater.from(context)
                ,parent
                ,false)
                .root

    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val model = list.get(position)
        ItemAlarmBinding.bind(holder.itemView).apply{
            hour.text = model.timeHour.toString()
            minuts.text = model.timeMinutes.toString()
            cancel.setOnClickListener {
                val alarmManager:AlarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
                val intent = Intent(context,alarmClass::class.java)
                val pedningIntent = PendingIntent.getBroadcast(context,model.id,intent, 0)
                alarmManager.cancel(pedningIntent)

                if(isringtoneInit()){
                ringtone.stop()}


                removeItem(position)
                mPrefs.setList(Constants.ALARM_KEY,list)
                notifyItemRemoved(position)
            }
        }
    }



    override fun getItemCount()=list.size
    fun removeItem(position: Int)
    {
        list.removeAt(position)
        mPrefs.setList(Constants.ALARM_KEY,list)
        notifyItemRemoved(position)
    }
}