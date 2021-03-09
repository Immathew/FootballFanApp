package com.example.footballfanapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TopLeaguesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TopLeaguesTypeConverter::class)
abstract class TopLeaguesDatabase: RoomDatabase() {

    abstract fun topLeaguesDao(): TopLeaguesDao
}