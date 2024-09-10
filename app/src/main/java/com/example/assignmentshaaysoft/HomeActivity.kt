package com.example.assignmentshaaysoft

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.progressindicator.CircularProgressIndicator

class HomeActivity : AppCompatActivity() {
    private lateinit var collarConnectionStatus: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
    }

}


