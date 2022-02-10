package com.hema.taqsawy.adapter

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.airbnb.lottie.LottieAnimationView
import com.hema.taqsawy.R
import com.hema.taqsawy.data.db.pojo.alarmModel.AlarmModel
import com.hema.taqsawy.ui.alarm.AlarmBCR
import com.hema.taqsawy.ui.alarm.AlarmViewModel
import kotlinx.android.synthetic.main.list_item_alarm.view.*


class AlarmAdapter(
    private val items: MutableList<AlarmModel>,
    alarmViewModel: AlarmViewModel,
    private var context: Context
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    private val alarViewModel: AlarmViewModel = alarmViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_alarm, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val weatherState = items[position].weatherState

        when (weatherState) {

            context.getString(R.string.alarm_rain) -> {
                holder.bigImage.setBackgroundResource(R.drawable.ic_settings)
                holder.circleImage.setAnimation(R.raw.moderate_rain)
                holder.accurateState.visibility = View.VISIBLE
            }
            context.getString(R.string.alarm_temp) -> {
                holder.bigImage.setBackgroundResource(R.drawable.ic_settings)
                holder.circleImage.setAnimation(R.raw.temp)
                holder.accurateState.visibility = View.VISIBLE
            }
            context.getString(R.string.alarm_wind) -> {
                holder.bigImage.setBackgroundResource(R.drawable.ic_settings)
                holder.circleImage.setAnimation(R.raw.wind)
                holder.accurateState.visibility = View.VISIBLE
            }
            context.getString(R.string.alarm_cloudness) -> {
                holder.bigImage.setBackgroundResource(R.drawable.ic_settings)
                holder.circleImage.setAnimation(R.raw.scattered_clouds)
                holder.accurateState.visibility = View.VISIBLE
            }
            context.getString(R.string.alarmThunder) -> {
                holder.bigImage.setBackgroundResource(R.drawable.ic_settings)
                holder.circleImage.setAnimation(R.raw.thunderstorm)
                holder.accurateState.visibility = View.GONE
            }
            context.getString(R.string.alarmfog) -> {
                holder.bigImage.setBackgroundResource(R.drawable.ic_settings)
                holder.circleImage.setAnimation(R.raw.mist)
                holder.accurateState.visibility = View.GONE
            }
            context.getString(R.string.alarm_snow) -> {
                holder.bigImage.setBackgroundResource(R.drawable.ic_settings)
                holder.circleImage.setAnimation(R.raw.snow)
                holder.accurateState.visibility = View.GONE
            }

        }

        holder.weatherState.text = weatherState
        holder.dayPicked.setSelectedDays(items[position].daily)
        holder.userDescription.text = items[position].userDescription
        holder.accurateState.text = items[position].accurateState

        holder.arrowBtn.setOnClickListener {
            if (holder.expandableView.visibility === View.GONE) {
                TransitionManager.beginDelayedTransition(holder.alarmCardView, AutoTransition())
                holder.expandableView.visibility = View.VISIBLE
                holder.arrowBtn.setBackgroundResource(R.drawable.ic_settings)
            } else {
                TransitionManager.beginDelayedTransition(holder.alarmCardView, AutoTransition())
                holder.expandableView.visibility = View.GONE
                holder.arrowBtn.setBackgroundResource(R.drawable.ic_settings)
            }
        }


        holder.deleteItem.setOnClickListener {
            deleteDialog(position)
        }


    }


    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var bigImage: ImageView = itemView.bigImage
        var circleImage: LottieAnimationView = itemView.circleImage
        var weatherState: TextView = itemView.weatherState
        var accurateState: TextView = itemView.lessMore
        var arrowBtn: Button = itemView.arrowBtn
        var expandableView: ConstraintLayout = itemView.expandableView
        var dayPicked: MaterialDayPicker = itemView.day_picked
        var userDescription: TextView = itemView.userDescription
        var alarmCardView: CardView = itemView.alarmCardView
        var deleteItem: ImageView = itemView.deleteImg


        init {
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
        }

    }

    private fun deleteDialog(pos: Int) {
        val builder = AlertDialog.Builder(context).setCancelable(false)
        builder.setTitle(R.string.deleteAlarm)
        builder.setMessage(R.string.alarmMessage)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            stopAlarm(items[pos].id)
            alarViewModel.deleteAlarmItem(items[pos].id)
            notifyDataSetChanged()
        }
        builder.setNegativeButton(R.string.no) { _, _ ->
        }
        builder.show()
    }


    private fun stopAlarm(id: Int) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmBCR::class.java)
        var pendingIntent =
            PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        pendingIntent = PendingIntent.getBroadcast(
            context,
            id + 2000,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent = PendingIntent.getBroadcast(
            context,
            id + 4000,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }

    fun setIncomingList(incomingList: List<AlarmModel>) {
        items.clear()
        items.addAll(incomingList)
        notifyDataSetChanged()
    }


}