package com.example.robustatask.data.network

import com.example.robustatask.utils.API_KEY
import com.example.robustatask.data.network.networkDataModels.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("data/2.5/weather?")
    fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = API_KEY,
        @Query("units") units: String = "metric"
    ): Observable<WeatherResponse?>
}