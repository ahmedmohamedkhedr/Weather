package com.example.robustatask.data.helpers.apiHelper

import com.example.robustatask.data.network.ApiService
import com.example.robustatask.data.network.networkDataModels.WeatherResponse
import io.reactivex.Observable

class ApiHelperImp(private val apiService: ApiService) : ApiHelper {

    override fun getWeatherDetails(lat: Double, lon: Double): Observable<WeatherResponse?> =
        apiService.getWeatherDetails(lat, lon)
}