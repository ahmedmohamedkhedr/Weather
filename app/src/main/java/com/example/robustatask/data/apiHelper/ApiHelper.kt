package com.example.robustatask.data.apiHelper

import com.example.robustatask.data.network.networkDataModels.WeatherResponse
import io.reactivex.Observable

interface ApiHelper {
    fun getWeatherDetails(lat: Double, lon: Double): Observable<WeatherResponse?>
}