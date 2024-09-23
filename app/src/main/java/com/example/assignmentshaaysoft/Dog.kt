package com.example.assignmentshaaysoft

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dogs")
data class Dog(
    @PrimaryKey(autoGenerate = true) val idn: Int = 0,
    val name: String,
    var batt: Int?,
    val id: String,
    var detectionLevel: Int,
    var mode: Int,
    var schedule: Schedule
) {
    // Secondary constructor with nullable parameters and default values
    constructor(
        idn: Long = 0L,
        name: String? = "Unknown",
        batt: Int? = 0
    ) : this(
        idn = idn.toInt(),
        name = name ?: "Unknown",
        batt = batt,
        id = "Unknown",  // Default value for ID
        detectionLevel = 0,  // Default detection level
        mode = 0,  // Default mode value
        schedule = Schedule( // Initialize schedule with default values
            monday = Array(24) { false },
            tuesday = Array(24) { false },
            wednesday = Array(24) { false },
            thursday = Array(24) { false },
            friday = Array(24) { false },
            saturday = Array(24) { false },
            sunday = Array(24) { false }
        )
    )


}

// Helper function to provide default empty schedule
fun emptySchedule(): Map<String, List<Int>> {
    return mapOf(
        "monday" to List(24) { 0 },
        "tuesday" to List(24) { 0 },
        "wednesday" to List(24) { 0 },
        "thursday" to List(24) { 0 },
        "friday" to List(24) { 0 },
        "saturday" to List(24) { 0 },
        "sunday" to List(24) { 0 }
    )
}

// Schedule data class (optional)
data class Schedule(
    var monday: Array<Boolean>,
    var tuesday: Array<Boolean>,
    var wednesday: Array<Boolean>,
    var thursday: Array<Boolean>,
    var friday: Array<Boolean>,
    var saturday: Array<Boolean>,
    var sunday: Array<Boolean>
)

