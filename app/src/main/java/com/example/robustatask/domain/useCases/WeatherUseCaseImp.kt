package com.example.robustatask.domain.useCases

import com.example.robustatask.R
import com.example.robustatask.data.apiHelper.ApiHelper
import com.example.robustatask.data.resourcesHelper.ResourcesHelper
import com.example.robustatask.domain.mapper.toWeatherDetailsModel
import com.example.robustatask.base.DataResponseStatus
import com.example.robustatask.domain.pojos.models.WeatherModel
import io.reactivex.Observable

class WeatherUseCaseImp(
    private val apiHelper: ApiHelper,
    private val resourcesHelper: ResourcesHelper
) : WeatherUseCase {

    override fun getWeatherDetails(
        lat: Double,
        lon: Double
    ): Observable<DataResponseStatus<WeatherModel>> {
        return apiHelper.getWeatherDetails(lat = lat, lon = lon).map {
            when (it) {
                null -> {
                    DataResponseStatus.Error<WeatherModel>(resourcesHelper.getStringRes(R.string.network_error))
                }
                else -> {
                    if (it.cod == 200) {
                        DataResponseStatus.Success<WeatherModel>(it.toWeatherDetailsModel())
                    } else {
                        DataResponseStatus.Error<WeatherModel>(resourcesHelper.getStringRes(R.string.an_error_happened))
                    }
                }
            }
        }
    }
}