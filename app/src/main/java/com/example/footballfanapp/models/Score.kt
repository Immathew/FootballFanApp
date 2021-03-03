package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class Score(
    @SerializedName("fullTime")
    val fullTime: FullTime?,
    @SerializedName("halfTime")
    val halfTime: HalfTime?,
)