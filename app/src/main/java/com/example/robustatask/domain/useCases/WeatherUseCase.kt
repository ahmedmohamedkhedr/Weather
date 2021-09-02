package com.example.robustatask.domain.useCases

import com.example.robustatask.base.DataResponseStatus
import com.example.robustatask.domain.pojos.models.WeatherModel
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import io.reactivex.Observable

interface WeatherUseCase {
    fun getWeatherDetails(lat: Double, lon: Double): Observable<DataResponseStatus<WeatherModel>>
    fun saveWeatherStory(weatherStoryModel: WeatherStoryModel): Observable<DataResponseStatus<Unit>>
    fun getHistory(): Observable<DataResponseStatus<MutableList<WeatherStoryModel>>>
    fun removeStory(id: String): Observable<DataResponseStatus<Unit>>

}