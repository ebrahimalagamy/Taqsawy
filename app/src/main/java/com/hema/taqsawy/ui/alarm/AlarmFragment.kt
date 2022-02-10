package com.hema.taqsawy.ui.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hema.taqsawy.R
import com.hema.taqsawy.adapter.AlarmAdapter
import com.hema.taqsawy.databinding.FragmentAlarmBinding
import com.hema.taqsawy.data.db.pojo.alarmModel.AlarmModel
import com.hema.taqsawy.providers.SharedPreferencesProvider
import java.text.SimpleDateFormat
import java.util.*


class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    lateinit var sharedPref: SharedPreferencesProvider

    lateinit var alarmViewModel: AlarmViewModel
    private var alarmList = mutableListOf<AlarmModel>()
    private lateinit var alarmAdapter: AlarmAdapter

    private var calenderEvent = Calendar.getInstance()

    private var repeating: Int = 24
    private val ONE_DAY_IN_SECONDS = 86400000
    private val TWO_DAYS_IN_SECONDS = 172800000
    private var alarmSwitchedOn: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        sharedPref = SharedPreferencesProvider(requireContext())

        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        alarmViewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(AlarmViewModel::class.java)


        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAlarm.setOnClickListener {
            binding.fabAlarm.collapse(true)
            binding.emptyImage.visibility = View.GONE
            binding.emptyListTxt.visibility = View.GONE
            val customDialog = CustomDialogChooseFragment(alarmViewModel)
            customDialog.show(parentFragmentManager, "m")

        }
        alarmAdapter = AlarmAdapter(ArrayList<AlarmModel>(), alarmViewModel, requireContext())
        binding.rvListWeatherHome.adapter = alarmAdapter
        binding.rvListWeatherHome.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvListWeatherHome.setHasFixedSize(true)

        alarmViewModel.fetchAlarmItems().observe(viewLifecycleOwner, Observer {

            alarmAdapter.setIncomingList(it)
            alarmList = it as MutableList<AlarmModel>
            if (it.isEmpty()) {
                Log.d("listttttttttttt", it.size.toString())

                binding.emptyImage.visibility = View.VISIBLE
                binding.emptyListTxt.visibility = View.VISIBLE
                binding.fabAlarm.visibility = View.VISIBLE
                binding.alarmCheck.isChecked = false
            }
        })



        alarmSwitchedOn = sharedPref.isAlarmSwitchedOn

        if (alarmSwitchedOn == true) {
            binding.imgAlarm.setBackgroundResource(R.drawable.alarm)
            binding.checkEventLayout.visibility = View.GONE
            binding.fornextradioGroup.visibility = View.GONE
            sharedPref.alarmSwitchedOn(true)
            binding.fabAlarm.visibility = View.GONE
            registerAll()
        } else {
            binding.imgAlarm.setBackgroundResource(R.drawable.alarm_off)
            binding.checkEventLayout.visibility = View.VISIBLE
            binding.fornextradioGroup.visibility = View.VISIBLE
            sharedPref.alarmSwitchedOn(false)
            binding.fabAlarm.visibility = View.VISIBLE
            unRegisterAll()

        }

        binding.alarmCheck.setOnClickListener(View.OnClickListener {
            if (binding.checkEventTimeTextInput == null) {
                binding.alarmCheck.isChecked = false
                Toast.makeText(requireContext(), getString(R.string.enterTime), Toast.LENGTH_SHORT)
                    .show()
            } else if (alarmList == null || alarmList.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.addAlertFirst),
                    Toast.LENGTH_SHORT
                ).show()
                binding.alarmCheck.isChecked = false
            } else if (binding.alarmCheck.isChecked) {
                Toast.makeText(requireContext(), getString(R.string.alarmon), Toast.LENGTH_SHORT)
                    .show()
                binding.imgAlarm.setBackgroundResource(R.drawable.alarm)
                binding.checkEventLayout.visibility = View.GONE
                binding.fornextradioGroup.visibility = View.GONE
                sharedPref.alarmSwitchedOn(true)
                binding.fabAlarm.visibility = View.GONE
                Toast.makeText(context, getString(R.string.addalarmperm), Toast.LENGTH_LONG).show()
                registerAll()
            } else {
                Toast.makeText(requireContext(), getString(R.string.alarmoff), Toast.LENGTH_SHORT)
                    .show()
                binding.imgAlarm.setBackgroundResource(R.drawable.alarm_off)
                binding.checkEventLayout.visibility = View.VISIBLE
                binding.fornextradioGroup.visibility = View.VISIBLE
                sharedPref.alarmSwitchedOn(false)
                binding.fabAlarm.visibility = View.VISIBLE
                unRegisterAll()
            }
        })

        binding.checkEventTimeTextInput.setOnClickListener(View.OnClickListener {
            calenderTime(
                binding.checkEventTimeTextInput,
                calenderEvent.time.hours,
                calenderEvent.time.minutes
            )

        })

        binding.fornextradioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.R24hr) {
                repeating = 24

            } else if (checkedId == R.id.R48hr) {
                repeating = 48

            } else {

                repeating = 72
            }
            Toast.makeText(requireContext(), "$repeating", Toast.LENGTH_SHORT).show()


        }


    }

    private fun calenderTime(checkEventTimeTextInput: EditText, hour: Int, min: Int) {

        TimePickerDialog(requireContext(), object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                calenderEvent = Calendar.getInstance()
                calenderEvent.set(Calendar.HOUR_OF_DAY, p1)
                calenderEvent.set(Calendar.MINUTE, p2)
                calenderEvent.set(Calendar.SECOND, 0)
                checkEventTimeTextInput.setText(SimpleDateFormat("HH:mm").format(calenderEvent.time))
                Log.i("timeinMilli", "${calenderEvent.timeInMillis}")

            }
        }, hour, min, false).show()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun registerAll() {
        val alarmIntent = Intent(context, AlarmBCR::class.java)
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (item in alarmList) {
            if (Calendar.getInstance().timeInMillis >= calenderEvent.timeInMillis) {
                alarmIntent.putExtra("ITEM_ID", item.id)
                var time = calenderEvent.timeInMillis
                calenderEvent.timeInMillis = time.plus(ONE_DAY_IN_SECONDS)
                var pendingIntent = PendingIntent.getBroadcast(
                    context,
                    item.id,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calenderEvent.timeInMillis,
                    pendingIntent
                )
                Log.d("taaaaaaaaaaaaaaageee24", "${calenderEvent.timeInMillis} llllllll")

                if (repeating == 48) {
                    pendingIntent = PendingIntent.getBroadcast(
                        context,
                        item.id + 2000,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calenderEvent.timeInMillis + ONE_DAY_IN_SECONDS,
                        pendingIntent
                    )
                    Log.d("48", "${calenderEvent.timeInMillis + ONE_DAY_IN_SECONDS}one")
                } else if (repeating == 72) {
                    pendingIntent = PendingIntent.getBroadcast(
                        context,
                        item.id + 2000,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calenderEvent.timeInMillis + ONE_DAY_IN_SECONDS,
                        pendingIntent
                    )
                    Log.d("48", "${calenderEvent.timeInMillis + ONE_DAY_IN_SECONDS}two")

                    pendingIntent = PendingIntent.getBroadcast(
                        context,
                        item.id + 4000,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calenderEvent.timeInMillis + TWO_DAYS_IN_SECONDS,
                        pendingIntent
                    )
                    Log.d("72", "${calenderEvent.timeInMillis + TWO_DAYS_IN_SECONDS}three")
                }
            } else {

                alarmIntent.putExtra("ITEM_ID", item.id)
                var pendingIntent = PendingIntent.getBroadcast(
                    context,
                    item.id,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calenderEvent.timeInMillis,
                    pendingIntent
                )
                Log.d("taaaaaaaaaaaaaaageee24", "${calenderEvent.timeInMillis}four")

                if (repeating == 48) {
                    pendingIntent = PendingIntent.getBroadcast(
                        context,
                        item.id + 2000,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calenderEvent.timeInMillis + ONE_DAY_IN_SECONDS,
                        pendingIntent
                    )
                    Log.d("48", "${calenderEvent.timeInMillis + ONE_DAY_IN_SECONDS}five")
                } else if (repeating == 72) {
                    pendingIntent = PendingIntent.getBroadcast(
                        context,
                        item.id + 2000,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calenderEvent.timeInMillis + ONE_DAY_IN_SECONDS,
                        pendingIntent
                    )
                    Log.d("48", "${calenderEvent.timeInMillis + ONE_DAY_IN_SECONDS}six")

                    pendingIntent = PendingIntent.getBroadcast(
                        context,
                        item.id + 4000,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calenderEvent.timeInMillis + TWO_DAYS_IN_SECONDS,
                        pendingIntent
                    )
                    Log.d("72", "${calenderEvent.timeInMillis + TWO_DAYS_IN_SECONDS}seven")
                }

            }
        }
    }


    private fun unRegisterAll() {
        for (item in alarmList) {
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmBCR::class.java)
            var pendingIntent = PendingIntent.getBroadcast(
                context,
                item.id,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent)
            }

            if (repeating == 72) {
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    item.id + 2000,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent)
                }
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    item.id + 4000,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent)
                }
            } else if (repeating == 48) {
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    item.id + 2000,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}