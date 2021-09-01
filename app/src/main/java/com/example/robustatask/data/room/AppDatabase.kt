package com.example.robustatask.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherEntity::class], version = AppDatabase.VERSION)

abstract class AppDatabase : RoomDatabase() {
    abstract val repoDao: RepoDao

    companion object {
        const val VERSION = 2
    }
}