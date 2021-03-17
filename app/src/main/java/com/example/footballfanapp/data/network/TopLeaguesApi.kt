package com.example.footballfanapp.data.network


import com.example.footballfanapp.models.*
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

    @GET("v2/teams/{id}")
    suspend fun getTeamDetails(
        @Path("id") teamId: Int
    ): Response<TeamDetails>

    @GET("v2/teams/{id}/matches")
    suspend fun getTeamUpcomingMatches(
        @Path("id") teamId: Int,
        @QueryMap queries: Map<String, String>
    ): Response<UpcomingMatchesModel>
}
