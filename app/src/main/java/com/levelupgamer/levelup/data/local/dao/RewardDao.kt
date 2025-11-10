package com.levelupgamer.levelup.data.local.dao

import androidx.room.*
import com.levelupgamer.levelup.model.Reward
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {
    @Query("SELECT * FROM rewards")
    fun getAllRewards(): Flow<List<Reward>>

    @Query("SELECT * FROM rewards WHERE id = :id")
    suspend fun getReward(id: String): Reward?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rewards: List<Reward>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReward(reward: Reward)

    @Update
    suspend fun updateReward(reward: Reward)

    @Delete
    suspend fun deleteReward(reward: Reward)
}
