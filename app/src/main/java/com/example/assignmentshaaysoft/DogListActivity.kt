package com.example.assignmentshaaysoft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class DogListActivity : AppCompatActivity() {

    private lateinit var dogRecyclerView: RecyclerView
    private lateinit var addButton: ImageView
    private lateinit var deleteButton: ImageView
    private val dogList = mutableListOf(
        Dog(1,"Granger Doggie", "",R.drawable.dog, "100%", "Online"),
        Dog(2,"Max Doggie","", R.drawable.dog, "100%", "Offline")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_list)

        // Initialize Views
        dogRecyclerView = findViewById(R.id.rvDogList)
        addButton = findViewById(R.id.btnAdd)
        deleteButton = findViewById(R.id.btnDelete)

        // Set up RecyclerView with Adapter
        val adapter = DogListAdapter(dogList)
        dogRecyclerView.layoutManager = LinearLayoutManager(this)
        dogRecyclerView.adapter = adapter

        // Button Click Listeners
        addButton.setOnClickListener {
            //dogList.add(Dog(3,"New Dog","", R.drawable.dog, "100%", "Offline"))
            showAddDogDialog(adapter)
            //adapter.notifyItemInserted(dogList.size - 1)
        }

        deleteButton.setOnClickListener {
            if (dogList.isNotEmpty()) {
                dogList.removeLast()
                adapter.notifyItemRemoved(dogList.size)
            }
        }
    }

    private fun showAddDogDialog(adapter: DogListAdapter) {
        // Create the custom dialog
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.dialog_add_dog, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)

        // Create and show the dialog
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        // Initialize dialog views
        val dogNameEditText: EditText = dialogView.findViewById(R.id.etDogName)
        val submitButton: Button = dialogView.findViewById(R.id.btnSubmit)
        val dogImage: ImageView = dialogView.findViewById(R.id.ivDogImage)
        val editIcon: ImageView = dialogView.findViewById(R.id.ivEditIcon)

        // Optional: Set click listener on edit icon to change image
        editIcon.setOnClickListener {
            // Handle image change logic here if needed
        }

        // Submit button click listener
        submitButton.setOnClickListener {
            val dogName = dogNameEditText.text.toString()
            if (dogName.isNotEmpty()) {
                // Add the new dog to the list
                dogList.add(Dog(5,dogName,"", R.drawable.dog, "100%", "Offline"))
                adapter.notifyItemInserted(dogList.size - 1)
                alertDialog.dismiss()
            }
        }
    }
}


