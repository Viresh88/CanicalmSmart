package com.example.assignmentshaaysoft

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu

class MainActivity : AppCompatActivity() {

    private val languages = arrayOf("English", "Italian", "French")
    private val languageFlags = intArrayOf(R.drawable.en, R.drawable.it, R.drawable.fr)

    private val devices = arrayOf("Device Name 1", "Device Name 2", "Device Name 3")
    private val deviceIcons = intArrayOf(R.drawable.ecllipse, R.drawable.ecllipse2, R.drawable.ecllipse3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // References to views
        val languageLayout: LinearLayout = findViewById(R.id.layout_language)
        val deviceLayout: LinearLayout = findViewById(R.id.layout_device)

        val languageIcon: ImageView = findViewById(R.id.img_language_icon)
        val languageText: TextView = findViewById(R.id.tv_language)

        val deviceIcon: ImageView = findViewById(R.id.img_device_icon)
        val deviceText: TextView = findViewById(R.id.tv_device)

        val addDeviceButton: ImageView = findViewById(R.id.btn_add_device)
        val submitButton: Button = findViewById(R.id.btn_submit)

        // Language dropdown functionality
        languageLayout.setOnClickListener {
            showPopupMenu(it, languages, languageFlags, languageText, languageIcon)
        }

        // Device dropdown functionality
        deviceLayout.setOnClickListener {
            showPopupMenu(it, devices, deviceIcons, deviceText, deviceIcon)
        }

        // Add Device Button
        addDeviceButton.setOnClickListener {
            Toast.makeText(this, "Add new device clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DogListActivity::class.java)
            startActivity(intent)
        }

        // Submit Button
        submitButton.setOnClickListener {
            val selectedLanguage = languageText.text.toString()
            val selectedDevice = deviceText.text.toString()
            Toast.makeText(this, "Selected: $selectedLanguage, $selectedDevice", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    // Function to show PopupMenu with icons
    private fun showPopupMenu(
        anchor: View,
        items: Array<String>,
        icons: IntArray,
        textView: TextView,
        imageView: ImageView
    ) {
        val popupMenu = PopupMenu(this, anchor)
        for (i in items.indices) {
            popupMenu.menu.add(0, i, i, items[i])
        }

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            textView.text = items[item.itemId]
            imageView.setImageResource(icons[item.itemId])
            true
        }

        popupMenu.show()
    }
}
