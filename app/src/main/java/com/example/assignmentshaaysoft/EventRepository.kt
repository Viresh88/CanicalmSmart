package com.example.assignmentshaaysoft

class EventRepository(private val eventDao: EventDao) {
    suspend fun getEventsForDay(dogId: String, minTime: Long, maxTime: Long): List<Event> {
        return eventDao.getEventsForDay(dogId, minTime, maxTime)
    }


    suspend fun getEvents(dogId: String): List<Event> {
        return eventDao.getEvents(dogId)
    }

    suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    suspend fun updateEvent(event: Event) {
        eventDao.updateEvent(event)
    }

    suspend fun getEventsForDog(dogId: String): List<Event> {
        return eventDao.getEventsForDog(dogId)
    }
}
