package com.example.footballfanapp.data.network


import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.models.UpcomingMatchesModel
import retrofit2.Response
import retrofit2.http.*

interface TopLeaguesApi {

    @GET("v2/competitions/")
    suspend fun getTopLeagues(
        @QueryMap queries: Map<String, String>
    ): Response<TopLeaguesModel>

    @GET("v2/matches/")
    suspend fun getUpcomingMatches(
        @QueryMap queries: Map<String, String>
    ): Response<UpcomingMatchesModel>
}
