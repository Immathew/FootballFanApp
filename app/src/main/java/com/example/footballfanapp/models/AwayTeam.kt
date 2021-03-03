package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class AwayTeam(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)