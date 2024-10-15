package com.example.assignmentshaaysoft

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private val context: Context,
    private val days: List<String>
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedPosition = -1 // Keep track of selected day

    inner class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayTextView: TextView = view.findViewById(R.id.dayTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val day = days[position]
        holder.dayTextView.text = day

        // Apply the selector based on the selected position
        holder.itemView.isSelected = position == selectedPosition

        // Handle click events to update selected day
        holder.itemView.setOnClickListener {
            if (day.isNotEmpty()) { // Ensure it's a valid day
                val previousSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelectedPosition) // Refresh the old selection
                notifyItemChanged(selectedPosition) // Refresh the new selection
            }
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }
}
