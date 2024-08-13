package com.example.assignmentshaaysoft

import LanguageAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Spinner

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val homeIcon = findViewById<ImageView>(R.id.homeIcon)
        val languageSpinner = findViewById<Spinner>(R.id.languageSpinner)
        val settingSpinner = findViewById<Spinner>(R.id.settingSpinner)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val pawIcon = findViewById<ImageView>(R.id.pawIcon)
        var btnBack = findViewById<ImageView>(R.id.btnBack)

        pawIcon.setOnClickListener {
            toggleIconVisibility()
        }

        homeIcon.setOnClickListener {
           val intent = Intent(this, HomeActivity::class.java)
           startActivity(intent)
        }

        val languageList = listOf(
            LanguageItem("English", R.drawable.en),
            LanguageItem("French", R.drawable.fr),
            LanguageItem("Italy", R.drawable.it)
            // Add more languages as needed
        )

        val adapter = LanguageAdapter(this, languageList)
        languageSpinner.adapter = adapter

        ArrayAdapter.createFromResource(
            this,
            R.array.stimulation,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            settingSpinner.adapter = adapter

        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handle the start of the touch event
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        btnBack.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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