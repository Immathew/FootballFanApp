package com.example.footballfanapp.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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
    val position: String?,
    @SerializedName("role")
    val role: String?
): Parcelable