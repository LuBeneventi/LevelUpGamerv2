package com.levelupgamer.levelup.data.local

import androidx.room.TypeConverter
import com.levelupgamer.levelup.model.RewardType

class Converters {
    @TypeConverter
    fun fromRewardType(value: RewardType): String {
        return value.name
    }

    @TypeConverter
    fun toRewardType(value: String): RewardType {
        return RewardType.valueOf(value)
    }
}
