package com.arunava.example.nasaastronomypictureoftheday.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arunava.example.nasaastronomypictureoftheday.databinding.ActivityLauncherBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class LauncherActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLauncherBinding.inflate(layoutInflater) }

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.date.text = simpleDateFormat.format(Calendar.getInstance().timeInMillis)
        binding.date.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(
                    this.simpleDateFormat.parse(binding.date.text as String?).time +
                            Calendar.getInstance().timeZone.getOffset(Date().time)
                )
                .setTitleText("Select date")
                .build()
            datePicker.addOnPositiveButtonClickListener {
                binding.date.text = simpleDateFormat.format(it)
            }
            datePicker.show(supportFragmentManager, "DatePicker")
        }

        binding.launcherButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("date", binding.date.text)
            }
            startActivity(intent)
        }
    }
}