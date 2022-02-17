package com.hema.taqsawy.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.hema.taqsawy.MainActivity
import com.hema.taqsawy.R
import kotlin.random.Random


class NotificationAlarm(val context: Context,val id:String) {
    val channelId = "channelId"
    val ChannelName = "ChannelName"
    val NOTIFICATIONID = Random(100).nextInt()
    val notificationManager:NotificationManagerCompat
    val notification: Notification
    var alarmUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    init {
        val view = RemoteViews(context.packageName, R.layout.notification_custom)
       // val intent = Intent(context, MainActivity::class.java)
//        val pendingIntent = TaskStackBuilder.create(context).run{
//            addNextIntentWithParentStack(intent)
//            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
//        }
        val alarmIntent = Intent(context,alarmClass::class.java)
        alarmIntent.putExtra("cancel","cancel")
        alarmIntent.putExtra("id",id)
        alarmIntent.putExtra("type","alarm")
        val pendingIntent_alarm =  PendingIntent.getBroadcast(context, Random(100).nextInt(),alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        view.setOnClickPendingIntent(R.id.cancel_alarm,pendingIntent_alarm)

        CreatenotificationChannel(context)
        notification = NotificationCompat.Builder(context,channelId)
//            .setContentTitle("notification")
//            .setContentText("this is notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCustomContentView(view)
            //.setContentIntent(pendingIntent)
//            .setWhen(0)
//            .setOnlyAlertOnce(true)
//            .addAction(R.mipmap.ic_launcher,"Cancel",pendingIntent_alarm)
            .setAutoCancel(true)
            .build()
         notificationManager = NotificationManagerCompat.from(context)


        Log.e("Alarm notification",id.toString())

    }
    fun notifyBtn()
    {
        notificationManager.notify(NOTIFICATIONID,notification)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        alarm.ringtone = RingtoneManager.getRingtone(context, alarmUri)
        alarm.ringtone.play()
        Log.e("Alarm notification",id.toString())
    }
    fun cancelNotification(){
        notificationManager.cancel(NOTIFICATIONID)
    }

fun CreatenotificationChannel(context: Context)
{
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val channel = NotificationChannel(
        channelId, ChannelName,
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        lightColor = Color.GREEN
        enableLights(true)
    }
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(channel)
}

}
}