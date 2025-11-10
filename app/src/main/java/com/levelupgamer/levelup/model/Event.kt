package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Modelo de Evento Actualizado
@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val date: String,
    val time: String,
    val locationName: String,
    val inscriptionPoints: Int,
    val prizePoints: Int
)
