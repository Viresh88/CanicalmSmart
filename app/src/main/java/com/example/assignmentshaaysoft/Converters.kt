package com.example.assignmentshaaysoft

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromSchedule(schedule: Schedule): String {
        return gson.toJson(schedule)
    }

    @TypeConverter
    fun toSchedule(data: String): Schedule {
        val type = object : TypeToken<Schedule>() {}.type
        return gson.fromJson(data, type)
    }
}
