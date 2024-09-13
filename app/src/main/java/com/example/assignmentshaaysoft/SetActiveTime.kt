package com.example.assignmentshaaysoft

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SetActiveTime : AppCompatActivity() {
    private lateinit var timeSlotButtons: List<Button>
    private var selectedButtons = mutableListOf<Button>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_active_time)
        val button12to1 = findViewById<Button>(R.id.btn_12am_1pm)
        val button1to2 = findViewById<Button>(R.id.btn_1pm_2pm)
        val button2to3 = findViewById<Button>(R.id.btn_2pm_3pm)


        // Add all the buttons to the list
        timeSlotButtons = listOf(button12to1, button1to2, button2to3)

        // Set the click listener for each button
        for (button in timeSlotButtons) {
            button.setOnClickListener {
                toggleButtonState(button)
            }
        }
    }

    private fun toggleButtonState(button: Button) {
        if (selectedButtons.contains(button)) {
            // Unselect the button
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.time_slot_unselected))
            button.setTextColor(ContextCompat.getColor(this, R.color.time_slot_text))
            selectedButtons.remove(button)
        } else {
            // Select the button
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.time_slot_selected))
            button.setTextColor(Color.WHITE) // Change text color to white
            selectedButtons.add(button)
        }
    }
}