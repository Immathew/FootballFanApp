package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class LeagueStanding(
    @SerializedName("standings")
    val standings: List<Standing>
)