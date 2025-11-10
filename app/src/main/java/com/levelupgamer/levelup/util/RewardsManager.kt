package com.levelupgamer.levelup.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.levelupgamer.levelup.model.Reward
import com.levelupgamer.levelup.model.UserLevel

object RewardsManager {
    private const val PREFS_NAME = "rewards_prefs"
    private const val KEY_POINTS = "user_points"
    private const val KEY_ACTIVE_REWARDS = "active_rewards"
    private val gson = Gson()

    private fun getPrefs(context: Context) = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // --- Points Management ---
    fun getPoints(context: Context): Int {
        return getPrefs(context).getInt(KEY_POINTS, 0)
    }

    fun addPoints(context: Context, pointsToAdd: Int) {
        val currentPoints = getPoints(context)
        getPrefs(context).edit().putInt(KEY_POINTS, currentPoints + pointsToAdd).apply()
    }

    fun usePoints(context: Context, pointsToUse: Int) {
        val currentPoints = getPoints(context)
        getPrefs(context).edit().putInt(KEY_POINTS, currentPoints - pointsToUse).apply()
    }
    
    fun getAutomaticDiscount(context: Context): Int {
        val email = UserManager.getUserEmail(context) ?: ""
        return if (email.endsWith("@duocuc.cl")) 15 else 0
    }

    fun getUserLevel(context: Context): UserLevel {
        val points = getPoints(context)
        return when {
            points >= UserLevel.VIP.requiredPoints -> UserLevel.VIP
            points >= UserLevel.GOLD.requiredPoints -> UserLevel.GOLD
            points >= UserLevel.SILVER.requiredPoints -> UserLevel.SILVER
            else -> UserLevel.BRONZE
        }
    }

    // --- Active Rewards Management ---
    fun getActiveRewards(context: Context): List<Reward> {
        val json = getPrefs(context).getString(KEY_ACTIVE_REWARDS, "[]")
        val type = object : TypeToken<List<Reward>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun activateReward(context: Context, reward: Reward) {
        val activeRewards = getActiveRewards(context).toMutableList()
        if (!activeRewards.any { it.id == reward.id }) {
            activeRewards.add(reward)
            val json = gson.toJson(activeRewards)
            getPrefs(context).edit().putString(KEY_ACTIVE_REWARDS, json).apply()
        }
    }

    fun useReward(context: Context, reward: Reward) {
        val activeRewards = getActiveRewards(context).toMutableList()
        activeRewards.removeAll { it.id == reward.id }
        val json = gson.toJson(activeRewards)
        getPrefs(context).edit().putString(KEY_ACTIVE_REWARDS, json).apply()
    }
}
