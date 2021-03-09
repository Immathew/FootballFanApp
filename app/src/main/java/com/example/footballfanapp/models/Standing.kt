package com.example.footballfanapp.models


import com.google.gson.annotations.SerializedName

data class Standing(
    @SerializedName("table")
    val table: List<Table>?
)