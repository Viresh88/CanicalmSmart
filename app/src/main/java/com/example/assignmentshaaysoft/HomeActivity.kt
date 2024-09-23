package com.example.assignmentshaaysoft

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator

class HomeActivity : AppCompatActivity() {
    private lateinit var circularProgressIndicator: CircularProgressIndicator
    private lateinit var progressText: TextView
    private val viewModel: EventViewModel by viewModels()

    private lateinit var detectedCount : TextView
    private lateinit var warnedCount : TextView
    private lateinit var correctedCount : TextView

    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val btnBack : ImageView = findViewById(R.id.btnBack)

        val pawIcon = findViewById<ImageView>(R.id.pawIcon)
        pawIcon.setOnClickListener {
            toggleIconVisibility()
        }
         val settingIcon = findViewById<ImageView>(R.id.settingIcon)

        settingIcon.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val txtDogName = findViewById<TextView>(R.id.txtDogName)
        val imgCalender = findViewById<ImageView>(R.id.imgCalender)
        val datetxtView = findViewById<TextView>(R.id.datetxtView)
        val detectedCount = findViewById<TextView>(R.id.detectedCount)
        val warnedCount = findViewById<TextView>(R.id.warnedCount)
        val correctedCount = findViewById<TextView>(R.id.correctedCount)

        val dogId = "dog123"
        val timestamp = System.currentTimeMillis()
        viewModel.getDayDataFromDB(timestamp, dogId) { hourlyData ->
            updateUI(hourlyData)
        }

        val dogName = intent.getStringExtra("DOG_NAME")
        if (!dogName.isNullOrEmpty()) {
            txtDogName.text = dogName
        } else{
            txtDogName.text = "No dog selected"
        }

        imgCalender.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create and show the DatePickerDialog
            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Display the selected date in the TextView
                    val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    datetxtView.text = selectedDate
                }, year, month, day)

            datePickerDialog.show()
        }

        val gauge = findViewById<SemiCircularGaugeView>(R.id.semiCircularGauge)
        gauge.setProgress(90f)

        val btnDetails : Button = findViewById(R.id.btnDetails)
        btnDetails.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        }

    private fun updateUI(hourlyData: List<HourlyData>) {
        var detectedTotal = 0
        var warnedTotal = 0
        var correctedTotal = 0

        hourlyData.forEach { data ->
            detectedTotal += data.totals[0]
            warnedTotal += data.totals[1]
            correctedTotal += data.totals[2]
        }

        detectedCount.text = "Detected: $detectedTotal"
        warnedCount.text = "Warned: $warnedTotal"
        correctedCount.text = "Corrected: $correctedTotal"
    }


    private fun toggleIconVisibility() {
        val iconLayout = findViewById<LinearLayout>(R.id.iconLayout)

        // Toggle visibility
        if (iconLayout.visibility == View.VISIBLE) {
            iconLayout.visibility = View.INVISIBLE
        } else {
            iconLayout.visibility = View.VISIBLE
        }
    }
    }


