package com.tejas.stackoverflow.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type

class ListToStringConverter : Serializable {

    @TypeConverter
    fun fromStringList(String: MutableList<String?>?): String? {
        if (String == null) {
            return null
        }
        val type: Type = object : TypeToken<MutableList<String?>?>() {}.type
        return Gson().toJson(String, type)
    }

    @TypeConverter
    fun toStringList(myString: String?): MutableList<String?>? {
        if (myString == null) {
            return null
        }
        val type: Type = object : TypeToken<MutableList<String?>?>() {}.type
        return Gson().fromJson<MutableList<String?>?>(myString, type)
    }
}