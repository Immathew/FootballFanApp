package com.example.footballfanapp.data.database

import androidx.room.TypeConverter
import com.example.footballfanapp.models.TopLeaguesModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TopLeaguesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun topLeaguesModelToString(topLeaguesModel: TopLeaguesModel): String {
        return gson.toJson((topLeaguesModel))
    }
    @TypeConverter
    fun stringToTopLeaguesModel(data: String): TopLeaguesModel {
        val listType = object : TypeToken<TopLeaguesModel>() {}.type
        return gson.fromJson(data, listType)
    }
}