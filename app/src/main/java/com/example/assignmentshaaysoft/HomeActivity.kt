package com.example.assignmentshaaysoft

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val pawIcon = findViewById<ImageView>(R.id.pawIcon)
        pawIcon.setOnClickListener {
            toggleIconVisibility()
        }
         val settingIcon = findViewById<ImageView>(R.id.settingIcon)

        settingIcon.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val imgCalender = findViewById<ImageView>(R.id.imgCalender)
        val datetxtView = findViewById<TextView>(R.id.datetxtView)

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


