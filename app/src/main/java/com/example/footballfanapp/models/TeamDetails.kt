package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class TeamDetails(
    @SerializedName("clubColors")
    val clubColors: String?,
    @SerializedName("crestUrl")
    val crestUrl: String?,
    @SerializedName("founded")
    val founded: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("squad")
    val squad: List<Squad>?,
    @SerializedName("venue")
    val venue: String?,
    @SerializedName("website")
    val website: String?
)