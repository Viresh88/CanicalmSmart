package com.example.assignmentshaaysoft

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout

class SetActiveTime : AppCompatActivity() {
    private lateinit var timeSlotButtons: List<Button>
    private var selectedButtons = mutableListOf<Button>()
    private lateinit var timeButtons: List<Button>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_set_active_time)



        val tabLayout = findViewById<TabLayout>(R.id.tabLayoutDays)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.view?.setBackgroundResource(R.drawable.tab_background_selector)  // Set selected tab background
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.view?.setBackgroundResource(R.drawable.tab_background_selector)  // Reset unselected tab background
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle reselection if needed
            }
        })



        timeButtons = listOf(
            findViewById(R.id.btn_12am_1pm),
            findViewById(R.id.btn_1pm_2pm),
            findViewById(R.id.btn_2pm_3pm),
            findViewById(R.id.btn_3pm_4pm),
            findViewById(R.id.btn_4pm_5pm),
            findViewById(R.id.btn_5pm_6pm),
            findViewById(R.id.btn_6pm_7pm),
            findViewById(R.id.btn_7pm_8pm),
            findViewById(R.id.btn_8pm_9pm),
            findViewById(R.id.btn_9pm_10pm),
            findViewById(R.id.btn_10pm_11pm),
            findViewById(R.id.btn_11pm_12pm),
            findViewById(R.id.btn_12pm_13pm),
            findViewById(R.id.btn_13pm_14pm),
            findViewById(R.id.btn_14pm_15pm),
            findViewById(R.id.btn_16pm_17pm),
            findViewById(R.id.btn_17pm_18pm),
            findViewById(R.id.btn_18pm_19pm),
            findViewById(R.id.btn_19pm_20pm),
            findViewById(R.id.btn_20pm_21pm)
        )

        // Set click listeners for each button
        for (button in timeButtons) {
            button.setOnClickListener {
                toggleButtonState(button)
            }
        }
    }

    // Function to toggle button state
    private fun toggleButtonState(button: Button) {
        // If the button is already selected, unselect it
        if (button.isSelected) {
            button.isSelected = false
            button.setBackgroundResource(R.drawable.bg_time_normal)
            button.setTextColor(Color.BLACK)
        } else {
            // Select the button
            button.isSelected = true
            button.setBackgroundResource(R.drawable.btn_time_selected)
            button.setTextColor(Color.WHITE)
        }
    }


    }


