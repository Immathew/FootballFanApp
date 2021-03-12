package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class TopScorers(
    @SerializedName("scorers")
    val scorers: List<Scorer>
)