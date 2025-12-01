package com.bindiya.notesappxml.core.model

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun fromTags(tags: List<String>): String = Gson().toJson(tags)

    @TypeConverter
    fun toTags(json: String): List<String> =
        Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
}