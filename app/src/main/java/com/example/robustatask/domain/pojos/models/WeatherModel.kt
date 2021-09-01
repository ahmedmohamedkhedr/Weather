package com.example.robustatask.domain.pojos.models

import com.example.robustatask.domain.pojos.enums.WindDirectionEnum

data class WeatherModel(
    val tempIcon: Int? = null,
    val tempDescription: String? = null,
    val temp: Int? = null,
    val maxTemp: Int? = null,
    val minTemp: Int? = null,
    val feelsLike: Int? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val countryCode: String? = null,
    val cityName: String? = null,
    val windSpeed: Double? = null,
    val windDirection: WindDirectionEnum? = null,
    val windVisibility: Int? = null,
    val updatedAt: Long? = null
)

