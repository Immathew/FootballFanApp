package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class UpcomingMatchesModel(

    @SerializedName("matches")
    val matches: List<Match>
)