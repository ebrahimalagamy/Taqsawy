package com.hema.taqsawy.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.hema.taqsawy.R
import com.hema.taqsawy.adapter.HourlyAdapter
import com.hema.taqsawy.adapter.DailyAdapter
import com.hema.taqsawy.data.network.response.DailyItem
import com.hema.taqsawy.data.network.response.HourlyItem
import com.hema.taqsawy.databinding.FragmentHomeBinding
import com.hema.taqsawy.internal.UnitSystem
import com.hema.taqsawy.providers.SharedPreferencesProvider
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

const val PERMISSION_ID = 100

class HomeFragment : Fragment() {
    private lateinit var dailyAdapter: DailyAdapter
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedPref: SharedPreferencesProvider
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var address: String = ""


    override fun onStart() {
        super.onStart()
        // Checking for first time launch - before calling setContentView()
        sharedPref = SharedPreferencesProvider(requireContext())
        if (sharedPref.isFirstTimeLaunch) { // if not the first time
            CheckStatus()
        }
    }

    fun CheckStatus() {
        if (!isConnected()) {
            showDialog()
            Toast.makeText(
                requireContext(),
                getString(R.string.connectionFailed),
                Toast.LENGTH_LONG
            ).show()
        } else if (isConnected()) {
            if (!checkLocation()) {
                showLocationDialog(getString(R.string.loc), getString(R.string.enablelocation))
            }
        }
        if (isConnected() && checkLocation()) {
            sharedPref.setFirstTimeLaunch(false)
        }

    }

    fun isConnected(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(getString(com.hema.taqsawy.R.string.checkInterNet))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.connect)) { dialog, which ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))

                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.exit)) { dialog, which ->
                requireActivity().finish()
                dialog.dismiss()
            }
            .show()
    }

    private fun showLocationDialog(alertTitle: String, message: String) {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(alertTitle)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.enablelocation_)) { dialog, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.exit)) { dialog, _ ->
                requireActivity().finish()
                dialog.dismiss()
            }
            .show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPreferencesProvider(requireActivity())
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLatestLocation()

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        bindUi()

    }

    private fun bindUi() {
        viewModel.getWeather().observe(viewLifecycleOwner) {
            if (it != null) {
                val daily: List<DailyItem?>? = it.daily
                dailyAdapter = DailyAdapter(requireActivity(), daily)
                binding.rvDailyWeather.adapter = dailyAdapter
                binding.rvDailyWeather.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                binding.rvDailyWeather.setHasFixedSize(true)
                dailyAdapter?.notifyDataSetChanged()
            }
            if (it != null) {
                val hourly: List<HourlyItem?>? = it.hourly
                hourlyAdapter = HourlyAdapter(requireContext(), hourly)
                binding.rvListWeatherHome.adapter = hourlyAdapter
                binding.rvListWeatherHome.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                binding.rvListWeatherHome.setHasFixedSize(true)
                hourlyAdapter?.notifyDataSetChanged()
                val description = it.current?.weather?.get(0)?.description

                binding.tvTempeatur.text =
                    String.format(
                        Locale.getDefault(),
                        "%.0f°${UnitSystem.tempUnit}",
                        it.current?.temp
                    )

                val geocoderAddres =
                    Geocoder(requireContext(), Locale(sharedPref.getLanguage.toString()))

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    fun getLatestLocation() {
        if (isPermissionGranted()) {
            if (checkLocation()) {
                val locationRequest = LocationRequest()
                with(locationRequest) {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 1000
                    numUpdates = 10
                }
                val fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity().application)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                showLocationDialog(
                    getString(R.string.location_),
                    getString(R.string.enablelocation)
                )
            }
        } else {
            requestPermission()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity().application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity().application,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocation(): Boolean {
        val locationManager =
            requireActivity().application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            sharedPref.setFirstTimeLocationenabled(true)
            return true
        } else {
            return false
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            // TODO use current location long and lat
            val lonDecimal = BigDecimal(location.longitude).setScale(4, RoundingMode.HALF_DOWN)
            val latDecimal = BigDecimal(location.latitude).setScale(4, RoundingMode.HALF_DOWN)
            sharedPref.setLatLong("$latDecimal", "$lonDecimal")
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLatestLocation()
        }
    }
    //400
}