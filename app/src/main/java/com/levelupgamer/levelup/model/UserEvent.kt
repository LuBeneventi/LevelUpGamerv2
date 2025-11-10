package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_events",
    primaryKeys = ["userId", "eventId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserEvent(
    val userId: Int,
    val eventId: String
)
