package com.example.assignmentshaaysoft

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE dogId = :dogId AND eventTimestamp >= :minTime AND eventTimestamp < :maxTime")
    suspend fun getEventsForDay(dogId: String, minTime: Long, maxTime: Long): List<Event>


    @Query("SELECT * FROM events WHERE dogId = :dogId")
    suspend fun getEvents(dogId: String): List<Event>
    @Insert
    suspend fun insertEvent(event: Event)

    // Updating an event with new penalization
    @Update
    suspend fun updateEvent(event: Event)

    // Retrieve events for a specific dog
    @Query("SELECT * FROM events WHERE dogId = :dogId")
    suspend fun getEventsForDog(dogId: String): List<Event>
}