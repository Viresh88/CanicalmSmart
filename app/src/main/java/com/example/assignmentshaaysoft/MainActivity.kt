package com.example.assignmentshaaysoft

import LanguageAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.assignmentshaaysoft.database.DogContract
import com.example.assignmentshaaysoft.database.DogDatabaseHelper

class MainActivity : AppCompatActivity() {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var isReceiverRegistered = false
    private lateinit var dialog: AlertDialog
    private lateinit var progressBar: ProgressBar
    private lateinit var messageTextView: TextView
    private lateinit var dogNameEditText: EditText
    private lateinit var okButton: Button

    private lateinit var dogDatabaseHelper: DogDatabaseHelper
    private lateinit var dogSpinner: Spinner
    private var isSpinnerInitialized = false

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    // Collar detected
                    showCollarDetectedDialog()
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                // No collar detected
                showCollarNotDetectedDialog()
            }
        }
    }

    private fun showCollarNotDetectedDialog() {
        progressBar.visibility = View.GONE
        messageTextView.text = "Collar not detected."
        okButton.visibility = View.VISIBLE

    }

    private fun showCollarDetectedDialog() {
        progressBar.visibility = View.GONE
        messageTextView.text = "Collar detected. Enter your dog's name:"
        dogNameEditText.visibility = View.VISIBLE
        okButton.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val languageSpinner: Spinner = findViewById(R.id.languageSpinner)
//        val spinner1: Spinner = findViewById(R.id.spinner1)
//        val spinner2 : Spinner = findViewById(R.id.spinner2)
        val btnAdd : ImageView = findViewById(R.id.btnAdd)

        dogDatabaseHelper = DogDatabaseHelper(this)
        dogSpinner = findViewById(R.id.spinner1)

        loadDogDataIntoSpinner()


//        ArrayAdapter.createFromResource(
//            this,
//            R.array.dropdown_items,
//            android.R.layout.simple_spinner_item
//
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner1.adapter = adapter
//        }


       bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        btnAdd.setOnClickListener {
            showAddDogDialog()
            startBluetoothDiscovery()
        }

//        ArrayAdapter.createFromResource(
//            this,
//            R.array.dropdown_items,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner2.adapter = adapter
//
//        }

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

    private fun navigateToHomePage(selectDog: Dog) {
          val intent = Intent(this, HomeActivity::class.java).apply {
              putExtra("DOG_NAME", selectDog.name)
          }
        startActivity(intent)
        finish()
    }

    private fun loadDogDataIntoSpinner() {
        val dogs = dogDatabaseHelper.getAllDogs()
        val dogNames = dogs.map { it.name }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dogNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dogSpinner.adapter = adapter

        dogSpinner.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isSpinnerInitialized) {
                    val selectedDog = dogs[position]
                    // Handle dog selection, e.g., display its details
                    Toast.makeText(
                        this@MainActivity,
                        "Selected: ${selectedDog.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToHomePage(selectedDog)
                } else{
                    isSpinnerInitialized = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where nothing is selected
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startBluetoothDiscovery() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        if (!isReceiverRegistered) {
            registerReceiver(bluetoothReceiver, filter)
            isReceiverRegistered = true
        }
        bluetoothAdapter.startDiscovery()
    }

    private fun showAddDogDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_dog, null)
        progressBar = dialogView.findViewById(R.id.progressBar)
        messageTextView = dialogView.findViewById(R.id.txtMessage)
        dogNameEditText = dialogView.findViewById(R.id.etDogName)
        okButton = dialogView.findViewById(R.id.btnOk)

        dialog = AlertDialog.Builder(this).setView(dialogView).setCancelable(false).create()
        dialog.show()

        okButton.setOnClickListener {
            if (dogNameEditText.visibility == View.VISIBLE) {
                val dogName = dogNameEditText.text.toString().trim()
                // Save dog name and return to home page
                if (dogName.isNotEmpty()) {
                    saveDogName(dogName)
                    dialog.dismiss()
                    returnToHomePage()
                }
                else {
                    Toast.makeText(this, "Please enter a dog name", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                dialog.dismiss()
            }
        }
        }

    private fun returnToHomePage() {
        finish()
    }

    private fun saveDogName(name: String) {
        val collarAddress = bluetoothAdapter.address  // Assuming this is how you get the collar's address
        saveDogToDatabase(name, collarAddress)
    }
    override fun onDestroy() {
        super.onDestroy()
        if (isReceiverRegistered) {
            unregisterReceiver(bluetoothReceiver)
            isReceiverRegistered = false
        }
    }

    private fun saveDogToDatabase(dogName: String, collarAddress: String) {
        val dbHelper = DogDatabaseHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DogContract.DogEntry.COLUMN_NAME, dogName)
            put(DogContract.DogEntry.COLUMN_COLLAR_ADDRESS, collarAddress)
        }

        val newRowId = db.insert(DogContract.DogEntry.TABLE_NAME, null, values)
        db.close()

        if (newRowId != -1L) {
            Toast.makeText(this, "Dog added successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error adding dog", Toast.LENGTH_SHORT).show()
        }
    }

}


