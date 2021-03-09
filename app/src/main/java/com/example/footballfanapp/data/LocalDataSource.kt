package com.example.footballfanapp.data

import com.example.footballfanapp.data.database.TopLeaguesDao
import com.example.footballfanapp.data.database.TopLeaguesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val topLeaguesDao: TopLeaguesDao
) {

    suspend fun insertTopLeagues(topLeaguesEntity: TopLeaguesEntity) {
        topLeaguesDao.insertTopLeagues(topLeaguesEntity)
    }

    fun readDatabase(): Flow<List<TopLeaguesEntity>> {
        return topLeaguesDao.readTopLeagues()
    }
}