package com.levelupgamer.levelup.data.repository

import com.levelupgamer.levelup.data.local.dao.EventDao
import com.levelupgamer.levelup.model.Event
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {

    fun getAllEvents(): Flow<List<Event>> {
        return eventDao.getAllEvents()
    }

    suspend fun getEventById(eventId: String): Event? {
        return eventDao.getEventById(eventId)
    }

    suspend fun insertEvent(event: Event) {
        eventDao.insertEvent(event)
    }

    suspend fun delete(event: Event) {
        eventDao.delete(event)
    }
}
