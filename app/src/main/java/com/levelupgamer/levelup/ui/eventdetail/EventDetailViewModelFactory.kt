package com.levelupgamer.levelup.ui.eventdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.levelupgamer.levelup.data.repository.EventRepository

class EventDetailViewModelFactory(
    private val eventRepository: EventRepository,
    private val eventId: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(eventRepository, eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
