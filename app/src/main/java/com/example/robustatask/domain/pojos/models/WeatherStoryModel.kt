package com.example.robustatask.domain.pojos.models

data class WeatherStoryModel(
    val ID: String,
    val thumbnail: String,
    val temp: String,
    val weatherDesc: String,
    val location: String,
    val createdAt: Long
)
