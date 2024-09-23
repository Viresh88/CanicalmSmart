package com.example.assignmentshaaysoft

import LanguageAdapter
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.assignmentshaaysoft.bluetooth.BluetoothManagerClass
import com.numaxes.canicomgps.injection.Injection
import com.numaxes.canicomgps.injection.ViewModelFactory


class MainActivity : AppCompatActivity()  {
    var animalAssociationDuration: Long = 30000
    private lateinit var bluetoothAdapter: BluetoothAdapter
     private var scanResultAdapter: BluetoothAdapterDevice? = null
    private var viewModel: DogViewModel? = null
    private var isReceiverRegistered = false
    private lateinit var dialog: AlertDialog
    private lateinit var progressBar: ProgressBar
    private lateinit var messageTextView: TextView
    private lateinit var dogNameEditText: EditText
    private lateinit var okButton: Button
     private var scanResults = mutableListOf<Device>()
    private lateinit var dogRepository : DogRepository

    private lateinit var dogSpinner: Spinner
    private var isSpinnerInitialized = false



    private fun showCollarNotDetectedDialog() {
        progressBar.visibility = View.GONE
        messageTextView.text = "Collar not detected."
        okButton.visibility = View.VISIBLE

    }


    private fun configureViewModel() {
        val modelFactory: ViewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProvider(this, modelFactory)[DogViewModel::class.java]
        BluetoothManagerClass.initializeBluetooth(this)

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
        configureViewModel()
        val languageSpinner: Spinner = findViewById(R.id.languageSpinner)

        val btnAdd : ImageView = findViewById(R.id.btnAdd)


        dogSpinner = findViewById(R.id.spinner1)

        loadDogDataIntoSpinner()



       bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        btnAdd.setOnClickListener {
            dogAdding()
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





    fun dogAdding() {
        val intent = Intent(this, BluetoothActivity::class.java)
        startActivity(intent)
    }


    private fun navigateToHomePage(selectDog: Dog?) {
          val intent = Intent(this, HomeActivity::class.java).apply {
              putExtra("DOG_NAME", selectDog!!.name)
          }
        startActivity(intent)
        finish()
    }

    private fun loadDogDataIntoSpinner() {
        val dogs: LiveData<List<Dog>>? = viewModel?.getAllDogs()
        var dogNamesList: List<String> = listOf()

        dogs?.observe(this) { dogList ->
            dogNamesList = dogList.map { it.name }
            for (name in dogNamesList) {
                Log.d("DogName", name)
            }
        }


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dogNamesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dogSpinner.adapter = adapter

        dogSpinner.onItemSelectedListener =object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isSpinnerInitialized) {
                    val selectedDog = dogs?.value?.get(position)
                    // Handle dog selection, e.g., display its details
                    Toast.makeText(
                        this@MainActivity,
                        "Selected: $selectedDog",
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







    private fun returnToHomePage() {
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
//
    }

}


