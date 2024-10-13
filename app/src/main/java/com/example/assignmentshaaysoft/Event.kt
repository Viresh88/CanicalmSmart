package com.example.assignmentshaaysoft

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dogId: String,
    val eventTimestamp: Long,
    val eventPenalization: Int
)