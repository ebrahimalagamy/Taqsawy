package com.hema.taqsawy.ui.favorite.favoriteDetails

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hema.taqsawy.R
import com.hema.taqsawy.adapter.MainAdapter
import com.hema.taqsawy.adapter.NextDayAdapter
import com.hema.taqsawy.data.network.response.DailyItem
import com.hema.taqsawy.data.network.response.HourlyItem
import com.hema.taqsawy.databinding.FragmentFavoriteDetailBinding
import com.hema.taqsawy.internal.UnitSystem
import com.hema.taqsawy.providers.SharedPreferencesProvider
import java.io.IOException
import java.util.*

class FavoriteDetailFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteDetailBinding
    private lateinit var nextDayAdapter: NextDayAdapter
    private lateinit var mainAdapter: MainAdapter
    private lateinit var favoriteDetailsViewModel: FavoriteDetailsViewModel
    private lateinit var sharedPref: SharedPreferencesProvider
    private lateinit var lang: String
    private var address: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                findNavController().navigate(R.id.action_favoriteDetailFragment_to_favouriteFragment)
//            }
//        })

        favoriteDetailsViewModel = ViewModelProvider(this)[FavoriteDetailsViewModel::class.java]

        //fetch favorite data from DB
        favoriteDetailsViewModel.getFavoriteWeatherData("32.15586", "33.15586").observe(viewLifecycleOwner) {
            if (it != null) {
                val daily: List<DailyItem?>? = it.daily
                nextDayAdapter = NextDayAdapter(requireActivity(), daily)
                binding.rvDailyWeather.adapter = nextDayAdapter
                binding.rvDailyWeather.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                binding.rvDailyWeather.setHasFixedSize(true)
                nextDayAdapter?.notifyDataSetChanged()
            }
            if (it != null) {
                val hourly: List<HourlyItem?>? = it.hourly
                mainAdapter = MainAdapter(requireActivity(), hourly)

                binding.rvListWeatherHome.adapter = mainAdapter
                binding.rvListWeatherHome.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                binding.rvListWeatherHome.setHasFixedSize(true)
                mainAdapter?.notifyDataSetChanged()

                val description = it.current?.weather?.get(0)?.description
                binding.tvTempeatur.text =
                    String.format(
                        Locale.getDefault(),
                        "%.0f°${UnitSystem.tempUnit}",
                        it.current?.temp
                    )

                val geocoderAddres =
                    Geocoder(requireActivity(), Locale(sharedPref.getLanguage.toString()))

                try {
                    if (sharedPref.getLanguage.toString() == "ar") {
                        address = geocoderAddres.getFromLocation(it.lat, it.lon, 1)[0].countryName
                            ?: it.timezone.toString()
                    } else {
                        address = geocoderAddres.getFromLocation(it.lat, it.lon, 1)[0].adminArea
                            ?: it.timezone.toString()
                        address += ",${
                            geocoderAddres.getFromLocation(
                                it.lat,
                                it.lon,
                                1
                            )[0].countryName ?: it.timezone.toString()
                        }"
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                binding.location.text = address
                binding.tvWeather.text = description.toString()
                binding.windSpeedTxt.text =
                    it.current?.windSpeed.toString() + " ${UnitSystem.WindSpeedUnit}"
                binding.humidityTxt.text = it.current?.humidity.toString() + " %"
                binding.pressure.text = it.current?.pressure.toString() + " hpa"
                binding.clouds.text = it.current?.clouds.toString() + " %"

            }
        }

    }

}