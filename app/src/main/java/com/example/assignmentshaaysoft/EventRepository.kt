package com.example.assignmentshaaysoft

class EventRepository(private val eventDao: EventDao) {
    suspend fun getEventsForDay(dogId: String, minTime: Long, maxTime: Long): List<Event> {
        return eventDao.getEventsForDay(dogId, minTime, maxTime)
    }
}
