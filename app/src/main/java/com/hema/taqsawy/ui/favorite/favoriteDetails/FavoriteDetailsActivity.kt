package com.hema.taqsawy.ui.favorite.favoriteDetails

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.matteobattilana.weather.PrecipType
import com.hema.taqsawy.adapter.DailyAdapter
import com.hema.taqsawy.adapter.HourlyAdapter
import com.hema.taqsawy.data.network.response.DailyItem
import com.hema.taqsawy.data.network.response.HourlyItem
import com.hema.taqsawy.databinding.ActivityFavoriteDetailsBinding
import com.hema.taqsawy.internal.UnitSystem
import com.hema.taqsawy.providers.SharedPreferencesProvider
import java.io.IOException
import java.util.*

class FavoriteDetailsActivity : AppCompatActivity() {
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var binding: ActivityFavoriteDetailsBinding
    private lateinit var favoriteDetailsViewModel: FavoriteDetailsViewModel
    private lateinit var sharedPref: SharedPreferencesProvider
    private lateinit var address: String
    private lateinit var weather: PrecipType

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteDetailsViewModel = ViewModelProvider(this)[FavoriteDetailsViewModel::class.java]
        bindUi()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun bindUi() {
        sharedPref = SharedPreferencesProvider(this)
        val lat = intent.getStringExtra("lat")
        val lng = intent.getStringExtra("lng")
        favoriteDetailsViewModel.getFavoriteWeatherData(lat, lng).observe(this) {
            if (it != null) {
                val daily: List<DailyItem?>? = it.daily
                dailyAdapter = DailyAdapter(applicationContext, daily)
                binding.rvDailyWeather.adapter = dailyAdapter
                binding.rvDailyWeather.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                binding.rvDailyWeather.setHasFixedSize(true)
                dailyAdapter?.notifyDataSetChanged()

                binding.progressbar.visibility = View.GONE
//                binding.tvCheckInternet.visibility = View.GONE

                val hourly: List<HourlyItem?>? = it.hourly
                hourlyAdapter = HourlyAdapter(this, hourly)
                binding.rvListWeatherHome.adapter = hourlyAdapter
                binding.rvListWeatherHome.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.rvListWeatherHome.setHasFixedSize(true)
                hourlyAdapter?.notifyDataSetChanged()

                val geocoderAddress =
                    Geocoder(this, Locale(sharedPref.getLanguage.toString()))

                try {
                    if (sharedPref.getLanguage.toString() == "ar") {
                        address = geocoderAddress.getFromLocation(it.lat, it.lon, 1)[0].countryName
                            ?: it.timezone.toString()
                        Log.e("locationCallback2", "${it.lat}" + "${it.lon}")

                    } else {
                        address = geocoderAddress.getFromLocation(it.lat, it.lon, 1)[0].adminArea
                            ?: it.timezone.toString()
                        address += ",${
                            geocoderAddress.getFromLocation(
                                it.lat,
                                it.lon,
                                1
                            )[0].countryName ?: it.timezone.toString()
                        }"
                    }
                    Log.e("locationCallback3", "${it.lat}" + "${it.lon}")

                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val description = it.current?.weather?.get(0)?.description
                if (description.toString() == "light rain" || description.toString() == "rain" ||
                    description.toString() == "moderate rain"||description.toString() == "heavy intensity rain"
                ) {
                    weather = PrecipType.RAIN
                    binding.wvWeatherView.apply {
                        setWeatherData(weather)
                    }

                } else if (description.toString() == "snow") {
                    weather = PrecipType.SNOW
                    binding.wvWeatherView.apply {
                        setWeatherData(weather)

                    }
                }
                binding.tvTemp.text =
                    String.format(
                        Locale.getDefault(),
                        "%.0f°${UnitSystem.tempUnit}",
                        it.current?.temp
                    )

                binding.location.text = address
                binding.tvWeatherDescription.text = description.toString()
                binding.windSpeedTxt.text = it.current?.windSpeed.toString()
                binding.windSpeedTxtUnit.text = UnitSystem.WindSpeedUnit
                binding.humidityTxt.text = it.current?.humidity.toString() + " %"
                binding.pressure.text = it.current?.pressure.toString()
                binding.clouds.text = it.current?.clouds.toString() + " %"
                binding.tempMax.text = daily?.get(0)?.temp?.max?.toInt().toString().plus("°")
                binding.tempMin.text = daily?.get(0)?.temp?.min?.toInt().toString().plus("°")

            } else {
                binding.progressbar.visibility = View.VISIBLE
                //  binding.tvCheckInternet.visibility = View.VISIBLE

            }
        }
    }

}
