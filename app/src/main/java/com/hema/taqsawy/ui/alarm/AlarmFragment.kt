package com.hema.taqsawy.ui.alarm

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.RadioGroup
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.hema.taqsawy.R
import com.hema.taqsawy.alarm.alarmClass
import com.hema.taqsawy.alarm.recyclerAdapter
import com.hema.taqsawy.data.alarmModel.AlarmModel
import com.hema.taqsawy.databinding.FragmentAlarmBinding
import com.hema.taqsawy.internal.Constants.Companion.ALARM_KEY
import com.hema.taqsawy.providers.SharedPreferencesProvider
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AlarmFragment : Fragment() {
    private lateinit var binding: FragmentAlarmBinding
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
     var modelList:ArrayList<AlarmModel>? = ArrayList()
    var size = 0
    var day_v =-1
    var year_v = -1
    var month_v =-1
    var hour_v = -1
    var minut_v =-1
    var notification_v = 0
    lateinit var shared:SharedPreferencesProvider
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shared = SharedPreferencesProvider(requireContext())
        if (shared != null) {
            modelList = shared.getModelList(ALARM_KEY)
            if (modelList != null) {
                binding.recyclerAlarms.apply {
                    adapter = recyclerAdapter(context, modelList!!)
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }
        }
        binding.floatingActionButton.setOnClickListener {
            dialog_init()
        }

    }

    fun dialog_init(){
        val dialog = MaterialDialog(requireContext())
            .customView(R.layout.custom_dialog)
        dialog.findViewById<Button>(R.id.date).setOnClickListener {
            handleDate()
        }
        dialog.findViewById<Button>(R.id.time).setOnClickListener {
            handleTime()
        }
        dialog.findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.radio_notification -> {
                    notification_v =0
                }
                R.id.radio_alarm ->{
                    notification_v = 1

                }
            }
        })
        dialog.findViewById<Button>(R.id.date_time_set).setOnClickListener {
            if (day_v != 0)
                if (month_v != 0)
                    if (year_v != 0)
                        if (hour_v != 0)
                            if (minut_v != 0)
                            {setAlarm()
                            dialog.dismiss()}
        }
        dialog.show()
    }

    private fun setAlarm() {
        val calendar =Calendar.getInstance()
//        calendar[Calendar.HOUR_OF_DAY] = hour_v
//        calendar[Calendar.MINUTE] = minut_v
//        calendar[Calendar.DAY_OF_WEEK] = day_v
//        calendar[Calendar.MONTH] = month_v
//        calendar[Calendar.YEAR] =year_v
        calendar.set(year_v,month_v,day_v,hour_v,minut_v,0)
        val time:Long = calendar.timeInMillis
//        var time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
//        if (System.currentTimeMillis() > time) {
//            // setting time as AM and PM
//            if (calendar.get(Calendar.AM_PM) == 0)
//                time = time + (1000 * 60 * 60 * 12);
//            else
//                time = time + (1000 * 60 * 60 * 24);
//        }
        val type :String =when(notification_v)
        {
            0->"notification"
            1->"alarm"
            else -> "notification"
        }
        if (modelList != null && modelList!!.size > 0){
            size = modelList!!.size

            modelList!!.add(AlarmModel(year_v,month_v,day_v,hour_v,minut_v, size))
        }else{
            size =0
            modelList = ArrayList()

            modelList!!.add(AlarmModel(year_v,month_v,day_v,hour_v,minut_v,size))
        }

        shared.setList(ALARM_KEY,modelList)

        alarmManager = requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val bundel = Bundle()
        val intent = Intent(requireActivity(), alarmClass::class.java)
        bundel.putString("id",size.toString())
        bundel.putString("cancel","null")
        bundel.putString("type",type)
        intent.putExtras(bundel)
        // we call broadcast using pendingIntent
        pendingIntent = PendingIntent.getBroadcast(requireActivity(), size, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        Log.e("AlarmFragment",time.toString())
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time,pendingIntent);

    }


    private val DatePickerDialogListener: DatePickerDialog.OnDateSetListener =
        object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
                day_v = day
                month_v =month
                year_v = year

            }
        }


    private fun handleDate() {
        val sdf = SimpleDateFormat("yy:MM:dd:hh:mm:ss")
        val currentDate = sdf.format(Date())
        val list = currentDate.split(":")
        Log.e("add",list.toString())

        val picker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DatePickerDialog(requireContext(),DatePickerDialogListener,list[0].toInt()+2000,list[1].toInt(),list[2].toInt())
                .show()
        } else {
            Log.e("AlarmFragment","not combatable")
        }
    }


    private val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
        object : TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
                hour_v =hour
                minut_v = minute
            }
        }


    private fun handleTime() {
        val sdf = SimpleDateFormat("yy:MM:dd:hh:mm:ss")
        val currentDate = sdf.format(Date())
        val list = currentDate.split(":")
        Log.e("add",list.toString())

        val picker = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TimePickerDialog(requireContext(),timePickerDialogListener,list[3].toInt(),list[4].toInt(), DateFormat.is24HourFormat(getActivity()))
                .show()

        } else {
            Log.e("AlarmFragment","not combatable")
        }
    }
}