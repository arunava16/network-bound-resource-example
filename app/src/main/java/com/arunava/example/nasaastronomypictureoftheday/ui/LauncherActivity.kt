package com.arunava.example.nasaastronomypictureoftheday.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arunava.example.nasaastronomypictureoftheday.databinding.ActivityLauncherBinding
import com.google.android.material.datepicker.*
import java.text.SimpleDateFormat
import java.util.*

class LauncherActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLauncherBinding.inflate(layoutInflater) }

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private val startDate = simpleDateFormat.parse("1995-06-16")
    private val today = simpleDateFormat.format(Calendar.getInstance().timeInMillis)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.date.text = "today"
        binding.date.setOnClickListener {
            val dateValidators = listOf(
                DateValidatorPointBackward.now(),
                DateValidatorPointForward.from(startDate.time)
            )
            val constraints = CalendarConstraints.Builder()
                .setValidator(CompositeDateValidator.allOf(dateValidators))
                .build()
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(getCurrentSelection())
                .setCalendarConstraints(constraints)
                .setTitleText("Select date")
                .build()
            datePicker.addOnPositiveButtonClickListener {
                val date = simpleDateFormat.format(it)
                binding.date.text = if (date == today) {
                    "today"
                } else {
                    date
                }
            }
            datePicker.show(supportFragmentManager, "DatePicker")
        }

        binding.launcherButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                if (binding.date.text.toString() != "today") {
                    putExtra("date", binding.date.text)
                }
            }
            startActivity(intent)
        }
    }

    private fun getCurrentSelection() = binding.date.text?.run {
        if (this == "today") {
            MaterialDatePicker.todayInUtcMilliseconds()
        } else {
            simpleDateFormat.parse(this.toString()).time +
                    Calendar.getInstance().timeZone.getOffset(Date().time)
        }
    }
}