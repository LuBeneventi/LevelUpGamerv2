package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class Reward(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val pointsCost: Int,

    // Nuevos campos para recompensas dinámicas
    val type: String = RewardType.DISCOUNT_PERCENTAGE.name,
    val value: Double? = null,
    val stock: Int? = null,
    val productCode: String? = null
)
