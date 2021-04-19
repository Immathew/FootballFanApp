package com.example.footballfanapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.footballfanapp.data.database.entities.FavoriteTeamEntity
import com.example.footballfanapp.data.database.entities.TopLeaguesEntity
import dagger.Provides
import kotlinx.coroutines.flow.Flow

@Dao
interface TopLeaguesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopLeagues(topLeaguesEntity: TopLeaguesEntity)

    @Query("SELECT * FROM top_leagues_table")
    fun readTopLeagues(): LiveData<List<TopLeaguesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTeam(favoriteTeamEntity: FavoriteTeamEntity)

    @Query("SELECT * FROM favorite_team_table")
    fun readFavoriteTeam(): LiveData<List<FavoriteTeamEntity>>

    @Delete
    suspend fun deleteFavoriteTeam(favoriteTeamEntity: FavoriteTeamEntity)
}