package com.example.assignmentshaaysoft

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class CalendarActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var backButton : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        spinnerMonth = findViewById(R.id.spinnerMonth)
        spinnerYear = findViewById(R.id.spinnerYear)
        backButton = findViewById(R.id.backButton)

        setupSpinners()

        backButton.setOnClickListener{
            onBackPressed()
        }


        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(this, "Selected date: $date", Toast.LENGTH_SHORT).show()
        }

    }


    private fun setupSpinners() {
        val months = arrayOf("January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December")

        // Use custom layout for spinner items
        ArrayAdapter(this, R.layout.custom_spinner_item, months).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMonth.adapter = adapter
        }

        // Set the default to the current month
        spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH))

        // Populate the year spinner dynamically
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val startYear = currentYear - 20
        val years = (startYear..currentYear).toList().sortedDescending().map { it.toString() }.toTypedArray()

        ArrayAdapter(this, R.layout.custom_spinner_item, years).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerYear.adapter = adapter
        }

        // Set the default to the current year
        spinnerYear.setSelection(0)
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}