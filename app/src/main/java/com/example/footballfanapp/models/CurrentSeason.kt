package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class CurrentSeason(
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("startDate")
    val startDate: String
)