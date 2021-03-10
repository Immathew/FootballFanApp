package com.example.footballfanapp.data

import com.example.footballfanapp.data.network.TopLeaguesApi
import com.example.footballfanapp.models.LeagueStanding
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.models.UpcomingMatchesModel
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val topLeaguesApi: TopLeaguesApi
) {

    suspend fun getTopLeagues(queries: Map<String, String>): Response<TopLeaguesModel> {
        return topLeaguesApi.getTopLeagues(queries)
    }
    suspend fun getUpcomingMatches(queries: Map<String, String>): Response<UpcomingMatchesModel> {
        return topLeaguesApi.getUpcomingMatches(queries)
    }

    suspend fun getLeagueStanding(leagueId: Int): Response<LeagueStanding> {
        return topLeaguesApi.getLeagueStanding(leagueId)
    }

}
