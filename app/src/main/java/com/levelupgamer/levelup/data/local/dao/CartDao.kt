package com.levelupgamer.levelup.data.local.dao

import androidx.room.*
import com.levelupgamer.levelup.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItem)

    @Update
    suspend fun update(item: CartItem)

    @Delete
    suspend fun delete(item: CartItem)

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: Int): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productCode = :productCode")
    suspend fun getCartItem(userId: Int, productCode: String): CartItem?

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)

    @Query("UPDATE cart_items SET quantity = :newQuantity WHERE userId = :userId AND productCode = :productCode")
    suspend fun updateQuantity(userId: Int, productCode: String, newQuantity: Int)
}
