package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "order_items",
    primaryKeys = ["orderId", "productCode"],
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
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
data class OrderItem(
    val orderId: String,
    val productCode: String,
    val quantity: Int
)
