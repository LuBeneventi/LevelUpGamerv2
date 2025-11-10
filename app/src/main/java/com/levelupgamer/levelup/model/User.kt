package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val rut: String,
    val birthDate: String,
    val phone: String,
    val passwordHash: String,
    val points: Int = 0,
    val referralCode: String = UUID.randomUUID().toString().take(6).uppercase(),
    val isActive: Boolean = true
)
