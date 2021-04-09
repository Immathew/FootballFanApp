package com.example.footballfanapp.data

import androidx.lifecycle.LiveData
import com.example.footballfanapp.data.database.TopLeaguesDao
import com.example.footballfanapp.data.database.entities.FavoriteTeamEntity
import com.example.footballfanapp.data.database.entities.TopLeaguesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val topLeaguesDao: TopLeaguesDao
) {

    suspend fun insertTopLeagues(topLeaguesEntity: TopLeaguesEntity) {
        topLeaguesDao.insertTopLeagues(topLeaguesEntity)
    }

    fun readTopLeagues(): LiveData<List<TopLeaguesEntity>> {
        return topLeaguesDao.readTopLeagues()
    }

    suspend fun insertFavoriteTeam(favoriteTeamEntity: FavoriteTeamEntity) {
        topLeaguesDao.insertFavoriteTeam(favoriteTeamEntity)
    }

    fun readFavoriteTeam(): Flow<List<FavoriteTeamEntity>> {
        return topLeaguesDao.readFavoriteTeam()
    }

    suspend fun deleteFavoritesTeam(favoriteTeamEntity: FavoriteTeamEntity) {
        topLeaguesDao.deleteFavoriteTeam(favoriteTeamEntity)
    }
}