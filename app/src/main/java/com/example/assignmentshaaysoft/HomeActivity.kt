package com.example.assignmentshaaysoft

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        setupProgressBar(detection = 15, warned = 20, corrected = 15)

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Home screen logic
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
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


    private fun setupProgressBar(detection: Int, warned: Int, corrected: Int) {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        progressBar.post {
            updateProgressBar(progressBar, detection, warned, corrected)
            updateTextColorsAndValues(detection, warned, corrected)
        }
    }


        private fun updateTextColorsAndValues(detection: Int, warned: Int, corrected: Int) {
            val tvDetection: TextView = findViewById(R.id.tvDetection)
            val tvWarned: TextView = findViewById(R.id.tvWarned)
            val tvCorrected: TextView = findViewById(R.id.tvCorrected)

            tvDetection.text = "Detection\n$detection"
            tvWarned.text = "Warned\n$warned"
            tvCorrected.text = "Corrected\n$corrected"

            // Set text colors to match progress bar segments
            tvDetection.setTextColor(resources.getColor(R.color.colorDetection))
            tvWarned.setTextColor(resources.getColor(R.color.colorWarned))
            tvCorrected.setTextColor(resources.getColor(R.color.colorCorrected))
        }






        private fun updateProgressBar(progressBar: ProgressBar, detection: Int, warned: Int, corrected: Int) {
        val total = (detection + warned + corrected).toFloat()  // Avoid division by zero
        if (total == 0f) return  // If total is zero, exit to avoid division by zero

        val maxProgressWidth = progressBar.width  // Now this should correctly fetch the width

        val detectionEndPoint = (detection / total) * maxProgressWidth
        val warnedEndPoint = ((detection + warned) / total) * maxProgressWidth

        val layerDrawable = progressBar.progressDrawable as LayerDrawable
        val detectionDrawable = layerDrawable.findDrawableByLayerId(R.id.detection_segment) as GradientDrawable
        val warnedDrawable = layerDrawable.findDrawableByLayerId(R.id.warned_segment) as GradientDrawable
        val correctedDrawable = layerDrawable.findDrawableByLayerId(R.id.corrected_segment) as GradientDrawable

        detectionDrawable.setBounds(0, 0, detectionEndPoint.toInt(), progressBar.height)
        warnedDrawable.setBounds(detectionEndPoint.toInt(), 0, warnedEndPoint.toInt(), progressBar.height)
        correctedDrawable.setBounds(warnedEndPoint.toInt(), 0, progressBar.width, progressBar.height)

        progressBar.invalidate()
    }



}


