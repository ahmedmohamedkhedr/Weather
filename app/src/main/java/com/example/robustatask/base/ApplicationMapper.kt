package com.example.robustatask.base

import com.example.robustatask.R
import com.example.robustatask.data.network.networkDataModels.WeatherResponse
import com.example.robustatask.data.room.WeatherEntity
import com.example.robustatask.domain.pojos.enums.WeatherIconEnum
import com.example.robustatask.domain.pojos.enums.WindDirectionEnum
import com.example.robustatask.domain.pojos.models.WeatherModel
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import com.example.robustatask.utils.FULL_DATE_FORMAT
import com.example.robustatask.utils.longToString
import java.util.*


fun getWeatherIcon(weatherStatus: String?): Int {
    return when (weatherStatus) {
        WeatherIconEnum.THUNDERSTORM.value -> R.drawable.ic_atmosphere
        WeatherIconEnum.DRIZZLE.value -> R.drawable.ic_drizzle
        WeatherIconEnum.RAIN.value -> R.drawable.ic_rain
        WeatherIconEnum.SNOW.value -> R.drawable.ic_snow
        WeatherIconEnum.ATMOSPHERE.value -> R.drawable.ic_atmosphere
        WeatherIconEnum.CLEAR.value -> R.drawable.ic_clear
        WeatherIconEnum.CLOUDS.value -> R.drawable.ic_cloudy
        WeatherIconEnum.EXTREME.value -> R.drawable.ic_extreme
        else -> R.drawable.ic_clear
    }
}


fun WeatherResponse.toWeatherDetailsModel(): WeatherModel {
    val details = weather.getOrNull(0)

    return WeatherModel(
        tempIcon = getWeatherIcon(details?.main),
        tempDescription = details?.description?.let {
            it.first().toUpperCase() + it.substring(
                1,
                it.length
            )
        },
        temp = main.temp.toInt(),
        maxTemp = main.temp_max.toInt(),
        minTemp = main.temp_min.toInt(),
        feelsLike = main.feels_like.toInt(),
        pressure = main.pressure,
        humidity = main.humidity,
        windSpeed = wind.speed,
        windDirection = wind.deg.convertToWindDirectionEnum(),
        windVisibility = visibility,
        updatedAt = Calendar.getInstance().timeInMillis,
        countryCode = sys.country,
        cityName = name
    )
}


private fun Int.convertToWindDirectionEnum(): WindDirectionEnum {
    return when (this) {
        0 -> WindDirectionEnum.E
        90 -> WindDirectionEnum.N
        180 -> WindDirectionEnum.W
        270 -> WindDirectionEnum.S
        in 0..90 -> WindDirectionEnum.NE
        in 90..180 -> WindDirectionEnum.NW
        in 180..270 -> WindDirectionEnum.SW
        else -> WindDirectionEnum.SE
    }
}

fun WeatherStoryModel.convertToWeatherEntity(): WeatherEntity {
    return WeatherEntity(
        storyId = ID,
        thumbnailPath = thumbnail,
        temp = temp,
        tempDescription = weatherDesc,
        updatedAt = longToString(createdAt, FULL_DATE_FORMAT),
        location = location
    )
}


fun WeatherModel.convertToWeatherStoryModel(thumbnailPath: String): WeatherStoryModel {
    return WeatherStoryModel(
        ID = UUID.randomUUID().toString(),
        thumbnail = thumbnailPath,
        temp = temp?.toString() ?: "",
        weatherDesc = tempDescription ?: "",
        createdAt = Calendar.getInstance().time.time,
        location = cityName ?: ""
    )
}


fun WeatherEntity.convertToWeatherStoryModel(): WeatherStoryModel {
    return WeatherStoryModel(
        ID = storyId,
        thumbnail = thumbnailPath,
        temp = temp,
        weatherDesc = tempDescription,
        location = location,
        createdAt = Calendar.getInstance().time.time
    )
}
