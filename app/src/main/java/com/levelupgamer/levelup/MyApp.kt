package com.levelupgamer.levelup

import android.app.Application
import com.levelupgamer.levelup.data.local.AppDatabase

class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp
    }

    val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}