package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class Squad(
    @SerializedName("squad")
    val players: List<Player>
)