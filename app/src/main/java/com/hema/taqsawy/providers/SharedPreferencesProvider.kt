package com.hema.taqsawy.providers

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesProvider(context: Context) {

    companion object {
        private lateinit var pref: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private const val PREF_NAME = "SHARED_PREFERENCE"
        private const val IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH"

        // Shared preferences for location
        private const val IS_LOCATION_ENABLED = "IS_LOCATION_ENABLED"

        // Shared preferences for LAT_LONG
        private const val LAT_SHARED_PREF = "LAT_SHARED_PREF"
        private const val LONG_SHARED_PREF = "LONG_SHARED_PREF"

        // Shared preferences for LAT_LONG_Fav
        private const val LAT_SHARED_PREF_FAV = "LAT_SHARED_PREF_FAV"
        private const val LONG_SHARED_PREF_FAV = "LONG_SHARED_PREF_FAV"

        // shared preference for units and language
        private const val UNITS_SHARED_PREF = "UNITS_SHARED_PREF"
        private const val LANGUAGE_SHARED_PREF = "LANGUAGE_SHARED_PREF"

    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor.commit()
    }

    val isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)

    fun setUnit(unit: String) {
        editor.putString(UNITS_SHARED_PREF, unit)
        editor.apply()
    }

    val getUnit: String?
        get() = pref.getString(UNITS_SHARED_PREF, "metric")

    fun setLanguage(Language: String) {
        editor.putString(LANGUAGE_SHARED_PREF, Language)
        editor.apply()
    }

    val getLanguage: String?
        get() = pref.getString(LANGUAGE_SHARED_PREF, "en")


    fun setFirstTimeLocationenabled(isFirstTime: Boolean) {
        editor.putBoolean(IS_LOCATION_ENABLED, isFirstTime)
        editor.commit()
    }

    fun setLatLong(latitude: String?, longitude: String?) {
        editor.putString(LAT_SHARED_PREF, latitude)
        editor.putString(LONG_SHARED_PREF, longitude)
        editor.commit()
    }

    val latLong: Array<String?>
        get() {
            val location = arrayOfNulls<String>(2)
            val lat = pref.getString(LAT_SHARED_PREF, null)
            val lng = pref.getString(LONG_SHARED_PREF, null)
            location[0] = lat
            location[1] = lng
            return location
        }

    fun setLatLongFav(latitude: String?, longitude: String?) {
        editor.putString(LAT_SHARED_PREF_FAV, latitude)
        editor.putString(LONG_SHARED_PREF_FAV, longitude)
        editor.commit()
    }

}