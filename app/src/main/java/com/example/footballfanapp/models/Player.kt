package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("countryOfBirth")
    val countryOfBirth: String?,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("nationality")
    val nationality: String?,
    @SerializedName("position")
    val position: String?
)