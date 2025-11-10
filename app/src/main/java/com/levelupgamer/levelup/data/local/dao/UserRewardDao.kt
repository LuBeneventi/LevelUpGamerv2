package com.levelupgamer.levelup.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.levelupgamer.levelup.model.UserReward
import kotlinx.coroutines.flow.Flow

@Dao
interface UserRewardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userReward: UserReward)

    @Query("SELECT * FROM user_rewards WHERE userId = :userId")
    fun getUserRewards(userId: Int): Flow<List<UserReward>>

    @Delete
    suspend fun delete(userReward: UserReward)
}
