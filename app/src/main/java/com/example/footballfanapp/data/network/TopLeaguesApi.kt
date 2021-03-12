package com.example.footballfanapp.data.network


import com.example.footballfanapp.models.LeagueStanding
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.models.TopScorers
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

    @GET("v2/competitions/{id}/standings")
    suspend fun getLeagueStanding(
        @Path("id") leagueId: Int
    ): Response<LeagueStanding>

    @GET("v2/competitions/{id}/matches")
    suspend fun getLeagueUpcomingMatches(
        @Path("id") leagueId: Int,
        @QueryMap queries: Map<String, String>
    ): Response<UpcomingMatchesModel>

    @GET("v2/competitions/{id}/scorers")
    suspend fun getLeagueTopScorers(
        @Path("id") leagueId: Int,
        @QueryMap queries: Map<String, String>
    ): Response<TopScorers>
}
