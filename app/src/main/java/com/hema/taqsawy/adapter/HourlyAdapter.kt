package com.hema.taqsawy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.hema.taqsawy.R
import com.hema.taqsawy.data.network.response.HourlyItem
import com.hema.taqsawy.internal.UnitSystem
import com.hema.taqsawy.providers.SharedPreferencesProvider
import kotlinx.android.synthetic.main.item_hourly.view.*
import java.text.SimpleDateFormat
import java.util.*


class HourlyAdapter(private val mContext: Context, private val items: List<HourlyItem?>?) :
    RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    private lateinit var sharedPref: SharedPreferencesProvider

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_hourly, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sharedPref = SharedPreferencesProvider(mContext)

        val data = items?.get(position)
        val simpleDateFormat = SimpleDateFormat("hh a", Locale(sharedPref.getLanguage.toString()))
        val format = simpleDateFormat.format(data?.dt?.times(1000L))

        holder.tvTime.text = format
        holder.tvTemp.text =
            String.format(Locale.getDefault(), "%.0f°${UnitSystem.tempUnit}", data?.temp)


        when (data?.weather?.get(0)?.icon) {
            "04d" -> {
                holder.iconTemp.setAnimation(R.raw.broken_clouds)
            }
            "04n" -> {
                holder.iconTemp.setAnimation(R.raw.broken_clouds)
            }
            "10d" -> {
                holder.iconTemp.setAnimation(R.raw.light_rain)
            }
            "10n" -> {
                holder.iconTemp.setAnimation(R.raw.light_rain)
            }
            "09d" -> {
                holder.iconTemp.setAnimation(R.raw.heavy_intentsity)
            }
            "09n" -> {
                holder.iconTemp.setAnimation(R.raw.heavy_intentsity)
            }
            "03d" -> {
                holder.iconTemp.setAnimation(R.raw.overcast_clouds)
            }
            "03n" -> {
                holder.iconTemp.setAnimation(R.raw.overcast_clouds)
            }

            "02d" -> {
                holder.iconTemp.setAnimation(R.raw.few_clouds)
            }
            "02n" -> {
                holder.iconTemp.setAnimation(R.raw.few_clouds)
            }

            "01d" -> {
                holder.iconTemp.setAnimation(R.raw.clear_sky)
            }
            "01n" -> {
                holder.iconTemp.setAnimation(R.raw.clear_sky)
            }
            "11d" -> {
                holder.iconTemp.setAnimation(R.raw.thunderstorm)
            }
            "11n" -> {
                holder.iconTemp.setAnimation(R.raw.thunderstorm)
            }
            "13d" -> {
                holder.iconTemp.setAnimation(R.raw.snow)
            }
            "13n" -> {
                holder.iconTemp.setAnimation(R.raw.snow)
            }
            "50d" -> {
                holder.iconTemp.setAnimation(R.raw.mist)
            }
            "50n" -> {
                holder.iconTemp.setAnimation(R.raw.mist)
            }

            else -> {
                holder.iconTemp.setAnimation(R.raw.unknown_icon)
            }
        }
    }

    override fun getItemCount(): Int {
        return items!!.size

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTime: TextView = itemView.tvTime
        var tvTemp: TextView = itemView.tvTemp
        var iconTemp: LottieAnimationView = itemView.iconTemp

    }
}
