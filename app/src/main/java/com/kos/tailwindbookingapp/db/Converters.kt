package com.kos.tailwindbookingapp.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {


    @TypeConverter
    fun fromPlayers(value: String): ArrayList<String> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<String>>() {
        }.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromPlayersString(value: ArrayList<String>): String {
        val gson = Gson()
        return gson.toJson(value)
    }



}
