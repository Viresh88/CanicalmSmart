package com.example.assignmentshaaysoft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentshaaysoft.R

class DogListAdapter(private val dogs: List<Dog>) :
    RecyclerView.Adapter<DogListAdapter.DogViewHolder>() {

    inner class DogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dogImage: ImageView = view.findViewById(R.id.ivDogImage)
        val dogName: TextView = view.findViewById(R.id.tvDogName)
        val batteryLevel: TextView = view.findViewById(R.id.tvBatteryLevel)
        val status: TextView = view.findViewById(R.id.tvStatus)
        val editIcon: ImageView = view.findViewById(R.id.ivEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogs[position]
        //holder.dogImage.setImageResource(dog.imageResId)
        holder.dogName.text = dog.name
        holder.batteryLevel.text = dog.batt.toString()
       // holder.status.text = dog.status
//        holder.status.setTextColor(
////            if (dog.status == "Online") holder.itemView.context.getColor(android.R.color.holo_green_dark)
////            else holder.itemView.context.getColor(android.R.color.darker_gray)
//        )
    }

    override fun getItemCount() = dogs.size
}
