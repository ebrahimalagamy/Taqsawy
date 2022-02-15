package com.hema.taqsawy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hema.taqsawy.R
import com.hema.taqsawy.data.network.response.DailyItem
import com.hema.taqsawy.databinding.ItemDailyBinding
import com.hema.taqsawy.internal.UnitSystem
import com.hema.taqsawy.providers.SharedPreferencesProvider
import java.text.SimpleDateFormat
import java.util.*


class DailyAdapter(private val mContext: Context, private val items: List<DailyItem?>?) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    lateinit var sharedPref: SharedPreferencesProvider
    private lateinit var binding: ItemDailyBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items?.get(position)
        sharedPref = SharedPreferencesProvider(mContext)

        val simpleDateFormat =
            SimpleDateFormat("EEE", Locale(sharedPref.getLanguage.toString()))
        val format = simpleDateFormat.format(data?.dt?.times(1000L))

        holder.binding.dailyDatetv.text = format
        holder.binding.tvDailyTemMax.text =data?.temp?.max?.toInt().toString().plus("°")
        holder.binding.tvDailyTempMin.text = data?.temp?.min?.toInt().toString().plus("°")


        when (data?.weather?.get(0)?.icon) {
            "04d" -> {
                holder.binding.iconTemp.setAnimation(R.raw.broken_clouds)
            }
            "04n" -> {
                holder.binding.iconTemp.setAnimation(R.raw.broken_clouds)
            }
            "10d" -> {
                holder.binding.iconTemp.setAnimation(R.raw.light_rain)
            }
            "10n" -> {
                holder.binding.iconTemp.setAnimation(R.raw.light_rain)
            }
            "09d" -> {
                holder.binding.iconTemp.setAnimation(R.raw.heavy_intentsity)
            }
            "09n" -> {
                holder.binding.iconTemp.setAnimation(R.raw.heavy_intentsity)
            }
            "03d" -> {
                holder.binding.iconTemp.setAnimation(R.raw.overcast_clouds)
            }
            "03n" -> {
                holder.binding.iconTemp.setAnimation(R.raw.overcast_clouds)
            }

            "02d" -> {
                holder.binding.iconTemp.setAnimation(R.raw.few_clouds)
            }
            "02n" -> {
                holder.binding.iconTemp.setAnimation(R.raw.few_clouds)
            }

            "01d" -> {
                holder.binding.iconTemp.setAnimation(R.raw.clear_sky)
            }
            "01n" -> {
                holder.binding.iconTemp.setAnimation(R.raw.clear_sky)
            }
            "11d" -> {
                holder.binding.iconTemp.setAnimation(R.raw.thunderstorm)
            }
            "11n" -> {
                holder.binding.iconTemp.setAnimation(R.raw.thunderstorm)
            }
            "13d" -> {
                holder.binding.iconTemp.setAnimation(R.raw.snow)
            }
            "13n" -> {
                holder.binding.iconTemp.setAnimation(R.raw.snow)
            }
            "50d" -> {
                holder.binding.iconTemp.setAnimation(R.raw.mist)
            }
            "50n" -> {
                holder.binding.iconTemp.setAnimation(R.raw.mist)
            }

            else -> {
                holder.binding.iconTemp.setAnimation(R.raw.unknown_icon)
            }
        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    inner class ViewHolder(val binding: ItemDailyBinding) :
        RecyclerView.ViewHolder(binding.root)


}