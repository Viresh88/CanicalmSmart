package com.example.assignmentshaaysoft



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class HistoryItem(val eventType: String, val time: String, val iconResId: Int)

class HistoryAdapter(private var historyList: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.historyIcon)
        val eventTypeTextView: TextView = itemView.findViewById(R.id.signText)
        val timeTextView: TextView = itemView.findViewById(R.id.timeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.iconImageView.setImageResource(currentItem.iconResId)
        holder.eventTypeTextView.text = currentItem.eventType
        holder.timeTextView.text = currentItem.time
    }

    override fun getItemCount() = historyList.size

    fun updateList(newList: List<HistoryItem>) {
        historyList = newList
        notifyDataSetChanged()
    }
}
