package com.example.assignmentshaaysoft

import androidx.room.Dao
import androidx.room.Query

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE dogId = :dogId AND eventTimestamp >= :minTime AND eventTimestamp < :maxTime")
    suspend fun getEventsForDay(dogId: String, minTime: Long, maxTime: Long): List<Event>
}