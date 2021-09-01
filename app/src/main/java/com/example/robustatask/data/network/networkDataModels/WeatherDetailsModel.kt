package com.example.robustatask.data.network.networkDataModels

data class WeatherDetailsModel(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)