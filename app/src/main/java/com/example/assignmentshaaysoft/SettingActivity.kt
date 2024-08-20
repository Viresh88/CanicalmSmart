package com.example.assignmentshaaysoft

import LanguageAdapter
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView

class SettingActivity : AppCompatActivity() {
    private val daysOfWeek = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val homeIcon = findViewById<ImageView>(R.id.homeIcon)
        val languageSpinner = findViewById<Spinner>(R.id.languageSpinner)
        val settingSpinner = findViewById<Spinner>(R.id.settingSpinner)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val pawIcon = findViewById<ImageView>(R.id.pawIcon)
        var btnBack = findViewById<ImageView>(R.id.btnBack)

        val btnOpenDialog = findViewById<ImageView>(R.id.btnOpenDialog)
        val btnTues = findViewById<ImageView>(R.id.btnTues)
        val btnWed = findViewById<ImageView>(R.id.btnWed)
        val btnThur = findViewById<ImageView>(R.id.btnThur)
        val btnFri = findViewById<ImageView>(R.id.btnFri)
        val btnSat = findViewById<ImageView>(R.id.btnSat)
        val btnSun = findViewById<ImageView>(R.id.btnSun)


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

        //open custom dialog code
        btnOpenDialog.setOnClickListener {
            showTimeSelectorDialog(0)
        }

        btnTues.setOnClickListener {
            showTimeSelectorDialog(0)
        }

        btnWed.setOnClickListener {
            showTimeSelectorDialog(0)
        }

        btnThur.setOnClickListener {
            showTimeSelectorDialog(0)
        }

        btnFri.setOnClickListener {
            showTimeSelectorDialog(0)
        }

        btnSat.setOnClickListener {
            showTimeSelectorDialog(0)
        }

        btnSun.setOnClickListener {
            showTimeSelectorDialog(0)
        }

        btnBack.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showTimeSelectorDialog(initialDayIndex:Int) {
           val dialog = Dialog(this)
           dialog.setContentView(R.layout.dialog_time_selector)
//           dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
           dialog.setCancelable(false)

           val dayTitle : TextView = dialog.findViewById(R.id.dayTitle)
           val btnPrev : ImageView = dialog.findViewById(R.id.btnPrev)
           val btnNext : ImageView = dialog.findViewById(R.id.btnNext)
           val btnClose : ImageView = dialog.findViewById(R.id.btnClose)

          var currIndex = initialDayIndex
          dayTitle.text = daysOfWeek[currIndex]

          btnNext.setOnClickListener {
               if (currIndex < daysOfWeek.size - 1){
                   currIndex++;
                   dayTitle.text = daysOfWeek[currIndex]
               }
          }

         btnPrev.setOnClickListener {
             if (currIndex > 0){
                 currIndex--;
                 dayTitle.text = daysOfWeek[currIndex]
             }
         }

         btnClose.setOnClickListener {
             dialog.dismiss()
         }

        dialog.show()
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