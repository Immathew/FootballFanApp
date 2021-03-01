package com.example.footballfanapp.data

import com.example.footballfanapp.data.network.TopLeaguesApi
import com.example.footballfanapp.models.TopLeaguesModel
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val topLeaguesApi: TopLeaguesApi
) {

    suspend fun getTopLeagues(queries: Map<String, String>): Response<TopLeaguesModel> {
        return topLeaguesApi.getTopLeagues(queries)
    }
}