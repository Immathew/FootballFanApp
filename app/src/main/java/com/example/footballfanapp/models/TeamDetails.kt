package com.example.footballfanapp.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
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
    val squad: @RawValue List<Player>,
    @SerializedName("venue")
    val venue: String?,
    @SerializedName("website")
    val website: String?
): Parcelable