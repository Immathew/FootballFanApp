package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class FullTime(
    @SerializedName("awayTeam")
    val awayTeam: Any?,
    @SerializedName("homeTeam")
    val homeTeam: Any?
)