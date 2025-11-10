package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "cart_items",
    primaryKeys = ["userId", "productCode"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["code"],
            childColumns = ["productCode"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItem(
    val userId: Int,
    val productCode: String,
    val quantity: Int
)
