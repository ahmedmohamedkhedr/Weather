package com.example.robustatask.data.room

import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "weather")
data class WeatherEntity(
    @NonNull
    @PrimaryKey
    @field:ColumnInfo(name = "storyId")
    var storyId: String = "",

    @ColumnInfo(name = "thumbnailPath")
    var thumbnailPath: String,

    @ColumnInfo(name = "lastName")
    var temp: String,

    @ColumnInfo(name = "tempDescription")
    var tempDescription: String,

    @ColumnInfo(name = "updatedAt")
    var updatedAt: String,

    @ColumnInfo(name = "location")
    var location: String
)