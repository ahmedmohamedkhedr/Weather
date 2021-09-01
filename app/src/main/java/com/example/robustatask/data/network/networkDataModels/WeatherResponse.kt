package com.example.robustatask.data.network.networkDataModels

data class WeatherResponse(
    val cod: Int,
    val dt: Int,
    val main: TemperatureModel,
    val weather: List<WeatherDetailsModel>,
    val wind: WindModel,
    val visibility: Int,
    val sys: CountryModel,
    val name: String
)