package com.example.robustatask.data.helpers.dbHelper

import com.example.robustatask.base.convertToWeatherEntity
import com.example.robustatask.data.room.WeatherDao
import com.example.robustatask.data.room.WeatherEntity
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import io.reactivex.Observable

class DbHelperImp(private val dao: WeatherDao) : DbHelper {

    override fun getWeatherStories(): Observable<List<WeatherEntity?>?>? {
        return dao.getWeatherDetailsList()
    }

    override fun insertWeatherDetails(model: WeatherStoryModel): Long? {
        return dao.insertWeatherDetails(model.convertToWeatherEntity())
    }

    override fun deleteStory(storyId: String): Int {
        return dao.deleteStory(storyId)
    }
}