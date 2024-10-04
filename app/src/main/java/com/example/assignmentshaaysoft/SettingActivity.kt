package com.example.assignmentshaaysoft


import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView


class SettingActivity : AppCompatActivity() {
    private val languages = arrayOf("English", "Italian", "French")
    private val languageFlags = intArrayOf(R.drawable.en, R.drawable.it, R.drawable.fr)

    private val stimulationlevel = arrayOf("Beep Sound", "Stimulation Light", "Stimulation Heavy", "Stimulation Progresssive")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val languageLayout: LinearLayout = findViewById(R.id.layout_language)
        val languageIcon: ImageView = findViewById(R.id.img_language_icon)
        val languageText: TextView = findViewById(R.id.tv_language)

        val stimulationLayout : ConstraintLayout = findViewById(R.id.layout_stimulation)
        val stimulationtxt : TextView = findViewById(R.id.txtStimulation)
        val imgStimulationDropdown : ImageView = findViewById(R.id.imgStimulationDropdown)

        val iconActiveTime : ImageView = findViewById(R.id.iconActiveTime)
        val setTimeLayout : ConstraintLayout = findViewById(R.id.setTimeLayout)

        languageLayout.setOnClickListener {
            showPopupMenu(it, languages, languageFlags, languageText, languageIcon)
        }

        stimulationLayout.setOnClickListener {
            showPopupMenu2(it, stimulationlevel, stimulationtxt)
        }

        setTimeLayout.setOnClickListener {
            val intent = Intent(this, SetActiveTime::class.java)
            startActivity(intent)
        }

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

    private fun showPopupMenu2(
        anchor: View,
        items: Array<String>,
        textView: TextView,
    ) {
        val popupMenu = PopupMenu(this, anchor)
        for (i in items.indices) {
            popupMenu.menu.add(0, i, i, items[i])
        }

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            textView.text = items[item.itemId]
            true
        }

        popupMenu.show()
    }



}