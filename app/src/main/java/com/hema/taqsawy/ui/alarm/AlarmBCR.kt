package com.hema.taqsawy.ui.alarm

import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.hema.taqsawy.R
import com.hema.taqsawy.data.db.ForecastDatabase
import com.hema.taqsawy.data.db.alarmModel.AlarmModel
import com.hema.taqsawy.data.db.weatherModel.CurrentWeatherModel
import com.hema.taqsawy.data.network.response.DailyItem
import com.hema.taqsawy.data.repository.Repository
import com.hema.taqsawy.providers.SharedPreferencesProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class AlarmBCR : BroadcastReceiver() {
    val notificationId = "NOTIFICATION_CHANNEL_ID"
    lateinit var con: Context
    lateinit var repo: Repository
    private lateinit var weatherResponse: CurrentWeatherModel
    private lateinit var alarmResponse: AlarmModel
    var result = ""

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {

        con = context

        val alarmId = intent.getIntExtra("ITEM_ID", -1)
        Log.d("Alarm recieved", alarmId.toString())
        repo = Repository(context.applicationContext as Application)
        repo.fetchDataForBCR()

        val sharedPref: SharedPreferencesProvider =
            SharedPreferencesProvider(context.applicationContext as Application)
        val latLong = sharedPref.latLong

        val lat = latLong[0]
        val lng = latLong[1]
        runBlocking(Dispatchers.IO) {
            weatherResponse =
                ForecastDatabase.getInstance(context.applicationContext as Application).weatherDao()
                    .getAllForBroadCast(lat, lng)
            alarmResponse =
                ForecastDatabase.getInstance(context.applicationContext as Application).alarmDao()
                    .getAlarmById(alarmId)
        }



        checkData(alarmResponse)
    }

    private fun checkData(alarmResponse: AlarmModel) {
        for (day in alarmResponse.daily) {
            when (day) {
                MaterialDayPicker.Weekday.SATURDAY -> {
                    checkWeatherDate(day, alarmResponse.weatherState)
                }
                MaterialDayPicker.Weekday.SUNDAY -> {
                    checkWeatherDate(day, alarmResponse.weatherState)
                }
                MaterialDayPicker.Weekday.MONDAY -> {
                    checkWeatherDate(day, alarmResponse.weatherState)
                }
                MaterialDayPicker.Weekday.TUESDAY -> {
                    checkWeatherDate(day, alarmResponse.weatherState)
                }
                MaterialDayPicker.Weekday.WEDNESDAY -> {
                    checkWeatherDate(day, alarmResponse.weatherState)
                }
                MaterialDayPicker.Weekday.THURSDAY -> {
                    checkWeatherDate(day, alarmResponse.weatherState)
                }
                MaterialDayPicker.Weekday.FRIDAY -> {
                    checkWeatherDate(day, alarmResponse.weatherState)
                }
            }
            if (result != "") {
                showNotification()
            }

        }

    }


    private fun checkWeatherDate(day: MaterialDayPicker.Weekday, weatherState: String) {
        for (element in weatherResponse.daily!!) {
            if (day.toString()
                    .lowercase(Locale.getDefault()) == getDayFromWeatherResponse(element!!.dt!!.toLong())
            ) {
                checkAlertTypeOfData(
                    weatherResponse.daily!!.indexOf(element),
                    weatherState,
                    day.toString()
                )
            }
        }
    }


    private fun getDayFromWeatherResponse(toLong: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = (toLong) * 1000L
        val date = SimpleDateFormat("EEEE")
        return date.format(cal.time).lowercase(Locale.getDefault())
    }

    private fun checkAlertTypeOfData(index: Int, weatherState: String, day: String) {

        val itemDay = weatherResponse.daily?.get(index)!!
        when (weatherState) {
            con.getString(R.string.alarm_Dialo_Rain) -> {
                checkRainState(itemDay, day)
            }
            con.getString(R.string.Alarm_dialo_temp) -> {
                checkTempState(itemDay, day)
            }
            con.getString(R.string.alarm_dialo_wind) -> {
                checkWindState(itemDay, day)
            }
            con.getString(R.string.alarm_dialo_fog) -> {
                checkFogMistHazeState(itemDay, day)
            }
            con.getString(R.string.alarm_dialog_snow) -> {
                checkSnowState(itemDay, day)
            }
            con.getString(R.string.alarm_dialog_cloud) -> {
                checkCloudState(itemDay, day)
            }
            con.getString(R.string.alarm_dialog_thunder) -> {
                checkThunderState(itemDay, day)
            }

        }

    }

    private fun checkThunderState(itemDay: DailyItem, day: String) {
        if (itemDay.weather?.get(0)?.main == con.getString(R.string.alarm_dialog_thunder)) {
            result += "\n$day is ThunderStorm day!!"
        }
    }

    private fun checkCloudState(itemDay: DailyItem, day: String) {
        if (itemDay.weather?.get(0)?.main == con.getString(R.string.alarm_dialog_cloud)) {
            if (alarmResponse.minMaxChoice == "more Than") {
                if (alarmResponse.value <= itemDay.clouds!!) {
                    result += "\n${day} Cloudiness: more than ${itemDay.clouds}"
                }
            } else {
                if (alarmResponse.value > itemDay.temp?.day!!) {
                    result += "\n${day} Cloudiness: more than ${itemDay.clouds}"
                }
            }
        }

    }

    private fun checkSnowState(itemDay: DailyItem, day: String) {
        if (itemDay.weather?.get(0)?.main == con.getString(R.string.alarm_dialog_snow)) {
            result += "\n${day} is Snowy day"
        }

    }

    private fun checkFogMistHazeState(itemDay: DailyItem, day: String) {
        if (itemDay.weather?.get(0)?.main == "Haze"
            || itemDay.weather?.get(0)?.main == "Mist"
            || itemDay.weather?.get(0)?.main == "Fog"
            || itemDay.weather?.get(0)?.main == con.getString(R.string.alarm_dialo_fog)
        ) {
            result += "\n${day} This day has Haze"
        }

    }

    private fun checkWindState(itemDay: DailyItem, day: String) {
        if (alarmResponse.minMaxChoice == "more Than") {
            if (alarmResponse.value <= itemDay.windSpeed!!) {
                result += "\n${day} Wind: more than ${itemDay.windSpeed}"
            }
        } else {
            if (alarmResponse.value > itemDay.windSpeed!!) {
                result += "\n${day} Wind: less than ${itemDay.windSpeed}"
            }
        }

    }

    private fun checkTempState(itemDay: DailyItem, day: String) {
        if (alarmResponse.minMaxChoice == "more Than") {
            if (alarmResponse.value <= itemDay.temp?.day!!) {
                result += "\n${day} Temperature more than:  ${itemDay.temp.day}"
            }
        } else {
            if (alarmResponse.value > itemDay.temp?.day!!) {
                result += "\n${day} Temperature less than:  ${itemDay.temp.day}"
            }
        }
    }

    private fun checkRainState(itemDay: DailyItem, day: String) {
        if (itemDay.weather?.get(0)?.main == con.getString(R.string.alarm_Dialo_Rain)) {
            if (alarmResponse.minMaxChoice == "more Than") {
                if (alarmResponse.value <= itemDay.rain!!) {
                    result += "\n${day} Rain more Than: ${itemDay.rain}"
                }
            } else {
                if (alarmResponse.value > itemDay.rain!!) {
                    result += "\n${day} Rain less Than: ${itemDay.rain}"
                }
            }
        }
    }

    private fun showNotification() {
        var builder: Notification.Builder
        val manager = con.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationId,
                "Weather Alert", NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
            builder = Notification.Builder(con, notificationId)
        } else {
            builder = Notification.Builder(con)
        }
        builder.setTicker(con.getString(R.string.alert))
        builder.setContentTitle("Weather Alert")
        builder.setContentText(con.getString(R.string.becareful))
        builder.style = Notification.BigTextStyle().bigText(result)
        val notification: Notification = builder.build()
        manager.notify(10, notification)
    }

}