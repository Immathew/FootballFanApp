package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class Match(
    @SerializedName("awayTeam")
    val awayTeam: AwayTeam?,
    @SerializedName("competition")
    val competition: Competition,
    @SerializedName("homeTeam")
    val homeTeam: HomeTeam,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("score")
    val score: Score?,
    @SerializedName("status")
    val status: String,
    @SerializedName("utcDate")
    val utcDate: String?
)