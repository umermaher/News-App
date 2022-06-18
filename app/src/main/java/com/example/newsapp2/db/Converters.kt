package com.example.newsapp2.db

import androidx.room.TypeConverter
import com.example.newsapp2.models.Source


class Converters {
    @TypeConverter
    fun fromSource(source: Source):String = source.name

    @TypeConverter
    fun toSource(name:String):Source = Source(name,name)
}