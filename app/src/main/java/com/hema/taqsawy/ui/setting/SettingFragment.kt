package com.hema.taqsawy.ui.setting

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.hema.taqsawy.R
import com.hema.taqsawy.databinding.FragmentSettingBinding
import com.hema.taqsawy.providers.SharedPreferencesProvider

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var sharedPref: SharedPreferencesProvider
    private var language: String = "en"
    private var units: String = "metric"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPreferencesProvider(requireContext())

        binding.toggleButtonGroupUnits.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.btnMetric -> {
                        units = "metric"
                    }
                    R.id.btnImperial -> {
                        units = "imperial"
                    }
                }
                sharedPref.setUnit(units)
            }
        }

        binding.toggleButtonGroupLanguage.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.arabicBtn -> {
                        language = "ar"
                    }
                    R.id.englishBtn -> {
                        language = "en"
                    }
                }
                sharedPref.setLanguage(language)
            }
        }
        binding.addAlarmBtn.setOnClickListener {
            requireActivity().recreate()
        }

    }

}