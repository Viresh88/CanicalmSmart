package com.example.assignmentshaaysoft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

class EventViewModel(private val repository: EventRepository) : ViewModel() {
    fun getDayDataFromDB(timestamp: Long, dogId: String, callback: (List<HourlyData>) -> Unit) {
        viewModelScope.launch {
            val midnightTimestamp = getMidnightTimestamp(timestamp)
            val timeZoneOffset = TimeZone.getDefault().getOffset(midnightTimestamp)
            val minTime = midnightTimestamp + timeZoneOffset
            val maxTime = minTime + 24 * 3600 * 1000

            val events = repository.getEventsForDay("test",minTime,maxTime)
           // val events = repository.getEvents("D123")
            val processedData = processEvents(events)
            callback(processedData)
        }
    }

    private fun processEvents(events: List<Event>): List<HourlyData> {
        val data = Array(24) { HourlyData(mutableListOf(), IntArray(3)) }
        events.forEach { event ->
            val hour = Calendar.getInstance().apply { timeInMillis = event.eventTimestamp }.get(Calendar.HOUR_OF_DAY)
            data[hour].events.add(event)
            when (event.eventPenalization) {
                1 -> data[hour].totals[0]++
                2 -> data[hour].totals[1]++
                3 -> data[hour].totals[2]++
            }
        }
        return data.toList()
    }

    fun applySanction(time: Long, newPenalization: Int) {
        viewModelScope.launch {
            // Update the event's penalization
            val newEvent = Event(
                dogId = "test",
                eventTimestamp = time,
                eventPenalization = newPenalization
            )




            repository.insertEvent(newEvent)
        }
    }

    fun getMidnightTimestamp(timestamp: Long): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }




}

data class HourlyData(
    val events: MutableList<Event>,
    val totals: IntArray
)
