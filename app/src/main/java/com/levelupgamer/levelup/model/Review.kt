package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productCode: String,
    val userId: Int,
    val userName: String,
    val rating: Int,
    val comment: String,
    val date: Long = System.currentTimeMillis()
)
