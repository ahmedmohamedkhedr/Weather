package com.example.robustatask.domain.useCases

import com.example.robustatask.base.DataResponseStatus
import com.example.robustatask.domain.pojos.models.WeatherModel
import io.reactivex.Observable

interface WeatherUseCase {
    fun getWeatherDetails(lat: Double, lon: Double): Observable<DataResponseStatus<WeatherModel>>
}