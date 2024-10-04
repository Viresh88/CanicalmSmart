package com.example.assignmentshaaysoft

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    private lateinit var collarConnectionStatus: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
       val titleTextView = findViewById<TextView>(R.id.titleTextView)
        // The text you want to display
        val text = "Canicalm Smart"

        // Create a SpannableString from the text
        val spannable = SpannableString(text)

        // Apply a different color to "Canicalm"
        val canicalmColor = Color.parseColor("#4A4A4A")
        spannable.setSpan(
            ForegroundColorSpan(canicalmColor), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Apply a different color to "Smart"
        val smartColor = Color.parseColor("#F89E24")
        spannable.setSpan(
            ForegroundColorSpan(smartColor), 9, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the spannable text to the TextView
        titleTextView.text = spannable

        collarConnectionStatus = findViewById(R.id.txtMessage)

        // Simulate connectivity
        checkCollarConnection()
        setupBarChart()
    }

    private fun checkCollarConnection() {
        val connected = true // Mocked value
        if (connected) {
            collarConnectionStatus.text = "Connected"
            collarConnectionStatus.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else {
            collarConnectionStatus.text = "Disconnected"
            collarConnectionStatus.setTextColor(ContextCompat.getColor(this, R.color.corrected))
        }
    }
    private fun setupBarChart() {
        val barChart = findViewById<BarChart>(R.id.barChart)

        val entries = listOf(
            BarEntry(0f, 15f),  // Detection
            BarEntry(1f, 20f),  // Warned
            BarEntry(2f, 15f)   // Corrected
        )

        val barDataSet = BarDataSet(entries, "Detection")
        val data = BarData(barDataSet)

        barChart.data = data
        barChart.invalidate() // Refresh chart

        //Handle navigation
        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Home screen logic
                    true
                }
                R.id.bark_history -> {
                    // Bark history logic
                    val intent = Intent(this, BarkHistory::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings -> {
                    // Settings screen logic
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

}


