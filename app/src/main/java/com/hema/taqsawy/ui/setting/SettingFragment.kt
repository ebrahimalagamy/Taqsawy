package com.hema.taqsawy.ui.setting

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.hema.taqsawy.R
import com.hema.taqsawy.providers.SharedPreferencesProvider

class SettingFragment : PreferenceFragmentCompat() {

    private lateinit var unitListPreference: ListPreference
    private lateinit var languageListPreference: ListPreference
    lateinit var sharedPref: SharedPreferencesProvider

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        sharedPref = SharedPreferencesProvider(requireContext())
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())

        unitListPreference = findPreference("UNIT_SYSTEM")!!
        languageListPreference = findPreference("LANGUAGE")!!

        unitListPreference.onPreferenceChangeListener = Preference
            .OnPreferenceChangeListener { _, value ->
                pref.edit().putString("UNIT_SYSTEM", value.toString()).apply()
                sharedPref.setUnit(value.toString())
                findNavController().navigate(R.id.homeFragment)
                true
            }
        languageListPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, value ->
                pref.edit().putString("LANGUAGE", value.toString()).apply()
                sharedPref.setLanguage(value.toString())
                findNavController().navigate(R.id.homeFragment)
                true
            }

    }
}
