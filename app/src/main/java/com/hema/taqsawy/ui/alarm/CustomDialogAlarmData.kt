package com.hema.taqsawy.ui.alarm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.hema.taqsawy.R
import com.hema.taqsawy.databinding.CustomDialogAlarmDataBinding
import com.hema.taqsawy.data.db.pojo.alarmModel.AlarmModel
import com.hema.taqsawy.providers.SharedPreferencesProvider
import java.util.*

class CustomDialogAlarmData(
    context: Context,
    private val alarmViewModel: AlarmViewModel
) : DialogFragment() {
    var sharedPref: SharedPreferencesProvider = SharedPreferencesProvider(context)

    private var _binding: CustomDialogAlarmDataBinding? = null
    private val binding get() = _binding!!


    var timeT: String = ""
    var timeT2: String = ""
    var lessMore: String = "less"
    private var numberText: String = "0.0"
    private var number: Double = 0.0
    private var accurateState: String = ""
    private var desc: String = ""
    private var weatherState: String = ""


    private var selectedDays: List<MaterialDayPicker.Weekday> =
        ArrayList<MaterialDayPicker.Weekday>()


    companion object {
        val TAG: String = "y"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomDialogAlarmDataBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val language = sharedPref.getLanguage
        binding.dayPicker.locale = Locale.forLanguageTag(language!!)

        val sentMessage = CustomDialogChooseFragment.sentMessage
        if (sentMessage == "Rain" || sentMessage == "امطار"
            || sentMessage == "Temperature" || sentMessage == "درجة حرارة" ||
            sentMessage == "Wind" || sentMessage == "رياح" || sentMessage == "Cloudiness" || sentMessage == "غيوم"
        ) {
            if (sentMessage == "Rain" || sentMessage == "امطار") binding.unitTxt.text = "mm"
            if (sentMessage == "Temperature" || sentMessage == "درجة حرارة") binding.unitTxt.text =
                "°C"
            if (sentMessage == "Wind" || sentMessage == "رياح") binding.unitTxt.text = "m/s"
            if (sentMessage == "Cloudiness" || sentMessage == "غيوم") binding.unitTxt.text = "%"

            binding.lessMoreRadioGroup.visibility = View.VISIBLE
            binding.lessMoreNumber.setText("0.0")
        } else {
            binding.lessMoreRadioGroup.visibility = View.GONE
            binding.lessMoreNumber.setText("1.0")
        }
        binding.lessMoreRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.lessThanBtn) {
                lessMore = "less Than"
            }
            if (checkedId == R.id.moreThanBtn) {
                lessMore = "more Than"
            }

        }

        binding.weatherStateTxt.text = sentMessage


        binding.dayPicker.setDaySelectionChangedListener {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
            selectedDays = it
        }


        binding.addAlarmBtn.setOnClickListener(View.OnClickListener {
            numberText = binding.lessMoreNumber.text.trim().toString()
            number = numberText.toDouble()
            accurateState = "$lessMore $numberText ${binding.unitTxt.text}"
            desc = binding.description.text.toString()
            weatherState = binding.weatherStateTxt.text as String

            if (selectedDays.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.selectday), Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener

            } else if (number <= 0.0) {
                Toast.makeText(requireContext(), getString(R.string.number0), Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener

            } else if (numberText.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.numberempty),
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener

            } else if (desc == "") {
                Toast.makeText(requireContext(), getString(R.string.enterdesc), Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener

            } else {
                val timeToAlarm: String = "Anytime"
                val alarmModel: AlarmModel = AlarmModel(
                    weatherState = weatherState,
                    accurateState = accurateState,
                    daily = selectedDays,
                    billingTime = timeToAlarm,
                    userDescription = desc,
                    minMaxChoice = lessMore,
                    value = number
                )
                //insert to alarm Db
                alarmViewModel.insertAlarmToDB(alarmModel)
                dialog?.dismiss()

            }


        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


