package com.example.footballfanapp

import com.example.footballfanapp.Constants.Companion.API_KEY
import com.example.footballfanapp.models.TopLeaguesModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface TopLeaguesApi {

    @Headers("X-Auth-Token: $API_KEY")
    @GET("/v2/competitions")
    suspend fun getTopLeagues(
        @QueryMap queries: Map<String, String>
    ): Response<TopLeaguesModel>

}