package com.example.footballfanapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.footballfanapp.data.database.entities.FavoriteTeamEntity
import com.example.footballfanapp.data.database.entities.TopLeaguesEntity


@Database(
    entities = [TopLeaguesEntity::class, FavoriteTeamEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TopLeaguesTypeConverter::class)
abstract class TopLeaguesDatabase : RoomDatabase() {

    abstract fun topLeaguesDao(): TopLeaguesDao

}