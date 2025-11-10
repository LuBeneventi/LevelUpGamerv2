package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.levelupgamer.levelup.R

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val code: String,
    val name: String,
    val category: String,
    val price: Int,
    val description: String = "",
    val quantity: Int = 1,
    val imageResId: Int = R.drawable.ic_launcher_foreground, // Campo actualizado para usar recursos drawable
    val averageRating: Float = 0f
)
