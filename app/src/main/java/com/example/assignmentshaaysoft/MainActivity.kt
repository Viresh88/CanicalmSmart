package com.example.assignmentshaaysoft

import LanguageAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val languageSpinner: Spinner = findViewById(R.id.languageSpinner)
        val spinner1: Spinner = findViewById(R.id.spinner1)
        val spinner2 : Spinner = findViewById(R.id.spinner2)

        ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner1.adapter = adapter

        }

        ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner2.adapter = adapter

        }

        // Create a list of languages with corresponding flag icons
        val languageList = listOf(
            LanguageItem("English", R.drawable.en),
            LanguageItem("French", R.drawable.fr),
            LanguageItem("Italy", R.drawable.it)
            // Add more languages as needed
        )

        val adapter = LanguageAdapter(this, languageList)
        languageSpinner.adapter = adapter


    }
}