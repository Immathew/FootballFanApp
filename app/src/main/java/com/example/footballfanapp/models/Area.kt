package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class Area(
    @SerializedName("ensignUrl")
    val ensignUrl: String?,
    @SerializedName("name")
    val name: String?
)