package com.example.robustatask.data.helpers.dbHelper

import com.example.robustatask.data.room.WeatherEntity
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import io.reactivex.Observable

interface DbHelper {
    fun getWeatherStories(): Observable<List<WeatherEntity?>?>?
    fun insertWeatherDetails(model: WeatherStoryModel): Long?
    fun deleteStory(storyId: String): Int
}