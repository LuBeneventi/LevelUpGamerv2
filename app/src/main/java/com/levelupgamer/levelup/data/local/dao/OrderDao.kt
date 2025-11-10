package com.levelupgamer.levelup.data.local.dao

import androidx.room.*
import com.levelupgamer.levelup.model.Order
import com.levelupgamer.levelup.model.OrderItem
import com.levelupgamer.levelup.model.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItem>)

    @Transaction
    suspend fun insertOrderWithItems(order: Order, items: List<OrderItem>) {
        insertOrder(order)
        insertOrderItems(items)
    }

    @Update
    suspend fun update(order: Order)

    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY date DESC")
    fun getOrdersForUser(userId: Int): Flow<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders ORDER BY date DESC")
    fun getAllOrders(): Flow<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: String): OrderWithItems?
}
