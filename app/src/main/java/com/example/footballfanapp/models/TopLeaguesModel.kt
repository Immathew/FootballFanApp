package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class TopLeaguesModel(
    @SerializedName("competitions")
    val competitions: List<Competition>,
)