package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_addresses")
data class UserAddress(
    @PrimaryKey val id: String,
    val userId: Int,
    val street: String,
    val numberOrApt: String,
    val commune: String,
    val region: String,
    var isPrimary: Boolean = false
)
