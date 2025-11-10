package com.levelupgamer.levelup.data.local.dao


import androidx.room.*
import com.levelupgamer.levelup.model.UserAddress
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM user_addresses WHERE userId = :userId")
    fun getAddressesForUser(userId: Int): Flow<List<UserAddress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(address: UserAddress)

    @Update
    suspend fun update(address: UserAddress)

    @Delete
    suspend fun delete(address: UserAddress)

    @Query("DELETE FROM user_addresses WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Int)

    @Query("UPDATE user_addresses SET isPrimary = 0 WHERE userId = :userId")
    suspend fun clearPrimaryFlags(userId: Int)

    @Query("UPDATE user_addresses SET isPrimary = 1 WHERE id = :addressId")
    suspend fun setPrimary(addressId: String)
}
