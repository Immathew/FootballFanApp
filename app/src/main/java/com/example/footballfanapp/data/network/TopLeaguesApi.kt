package com.example.footballfanapp.data.network


import com.example.footballfanapp.models.TopLeaguesModel
import retrofit2.Response
import retrofit2.http.*

interface TopLeaguesApi {

    @GET("v2/competitions/")
    suspend fun getTopLeagues(
        @QueryMap queries: Map<String, String>
    ): Response<TopLeaguesModel>

}
