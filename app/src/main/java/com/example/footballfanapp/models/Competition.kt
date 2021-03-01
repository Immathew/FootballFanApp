package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class Competition(
    @SerializedName("area")
    val area: Area,
    @SerializedName("code")
    val code: String,
    @SerializedName("currentSeason")
    val currentSeason: CurrentSeason,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
)