package com.example.footballfanapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.footballfanapp.models.TopLeaguesModel
import com.example.footballfanapp.util.Constants.Companion.TOP_LEAGUES_TABLE

@Entity(tableName = TOP_LEAGUES_TABLE)
class TopLeaguesEntity(
    var topLeaguesModel: TopLeaguesModel
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}