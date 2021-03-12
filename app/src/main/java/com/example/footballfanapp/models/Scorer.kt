package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class Scorer(
    @SerializedName("numberOfGoals")
    val numberOfGoals: Int?,
    @SerializedName("player")
    val player: Player?,
    @SerializedName("team")
    val team: Team?
)