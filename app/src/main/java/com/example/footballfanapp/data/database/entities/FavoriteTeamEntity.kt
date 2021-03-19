package com.example.footballfanapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.footballfanapp.util.Constants.Companion.FAVORITE_TEAM_TABLE

@Entity(tableName = FAVORITE_TEAM_TABLE)
class FavoriteTeamEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val teamId: Int = 0
)