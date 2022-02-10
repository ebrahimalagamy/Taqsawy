package com.hema.taqsawy.ui.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hema.taqsawy.R
import com.hema.taqsawy.databinding.CustomDialogBinding

class CustomDialogChooseFragment(private val alarmViewModel: AlarmViewModel) : DialogFragment() {


    private var _binding: CustomDialogBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val TAG: String = "m"
        lateinit var sentMessage: String
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val customDialog2 = CustomDialogAlarmData(requireContext(), alarmViewModel)
        _binding = CustomDialogBinding.inflate(inflater, container, false)
        binding.cvRain.setOnClickListener(View.OnClickListener {
            sentMessage = getString(R.string.alarm_Dialo_Rain)
            dialog?.dismiss()
            customDialog2.show(parentFragmentManager, "y")

        })
        binding.cvTemperature.setOnClickListener(View.OnClickListener {
            sentMessage = getString(R.string.Alarm_dialo_temp)
            dialog?.dismiss()
            customDialog2.show(parentFragmentManager, "y")

        })
        binding.cvWind.setOnClickListener(View.OnClickListener {
            sentMessage = getString(R.string.alarm_dialo_wind)
            dialog?.dismiss()
            customDialog2.show(parentFragmentManager, "y")

        })
        binding.cvClouds.setOnClickListener(View.OnClickListener {
            sentMessage = getString(R.string.alarm_dialog_cloud)
            dialog?.dismiss()
            customDialog2.show(parentFragmentManager, "y")

        })
        binding.cvThunder.setOnClickListener(View.OnClickListener {
            sentMessage = getString(R.string.alarm_dialog_thunder)
            dialog?.dismiss()
            customDialog2.show(parentFragmentManager, "y")

        })
        binding.cvHaze.setOnClickListener(View.OnClickListener {
            sentMessage = getString(R.string.alarm_dialo_fog)
            dialog?.dismiss()
            customDialog2.show(parentFragmentManager, "y")

        })
        binding.cvSnow.setOnClickListener(View.OnClickListener {
            sentMessage = getString(R.string.alarm_dialog_snow)
            dialog?.dismiss()
            customDialog2.show(parentFragmentManager, "y")

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}