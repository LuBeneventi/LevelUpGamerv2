package com.levelupgamer.levelup.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_rewards",
    primaryKeys = ["userId", "rewardId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Reward::class,
            parentColumns = ["id"],
            childColumns = ["rewardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserReward(
    val userId: Int,
    val rewardId: String
)
