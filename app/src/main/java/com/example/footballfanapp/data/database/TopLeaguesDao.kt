package com.example.footballfanapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TopLeaguesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopLeagues(topLeaguesEntity: TopLeaguesEntity)

    @Query("SELECT * FROM top_leagues_table")
    fun readTopLeagues(): Flow<List<TopLeaguesEntity>>


}