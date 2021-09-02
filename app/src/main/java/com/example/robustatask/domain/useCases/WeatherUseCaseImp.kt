package com.example.robustatask.domain.useCases

import com.example.robustatask.R
import com.example.robustatask.data.helpers.apiHelper.ApiHelper
import com.example.robustatask.data.helpers.resourcesHelper.ResourcesHelper
import com.example.robustatask.base.toWeatherDetailsModel
import com.example.robustatask.base.DataResponseStatus
import com.example.robustatask.base.convertToWeatherStoryModel
import com.example.robustatask.data.helpers.dbHelper.DbHelper
import com.example.robustatask.domain.pojos.models.WeatherModel
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import io.reactivex.Observable

class WeatherUseCaseImp(
    private val apiHelper: ApiHelper,
    private val dbHelper: DbHelper,
    private val resourcesHelper: ResourcesHelper
) : WeatherUseCase {

    override fun getWeatherDetails(
        lat: Double,
        lon: Double
    ): Observable<DataResponseStatus<WeatherModel>> {
        return apiHelper.getWeatherDetails(lat = lat, lon = lon).map {
            if (it.cod == 200) {
                DataResponseStatus.Success<WeatherModel>(it.toWeatherDetailsModel())
            } else {
                DataResponseStatus.Error<WeatherModel>(resourcesHelper.getStringRes(R.string.an_error_happened))
            }
        }
    }

    override fun saveWeatherStory(weatherStoryModel: WeatherStoryModel): Observable<DataResponseStatus<Unit>> {
        val result = dbHelper.insertWeatherDetails(weatherStoryModel)
        return if (result != null)
            Observable.just(DataResponseStatus.Success<Unit>(Unit))
        else Observable.just(DataResponseStatus.Error<Unit>(resourcesHelper.getStringRes(R.string.error_saving_weather_story)))
    }

    override fun getHistory(): Observable<DataResponseStatus<MutableList<WeatherStoryModel>>> {
        val result = dbHelper.getWeatherStories()
        return if (result != null) {
            result.flatMap { it ->
                val weatherStoriesList = it.mapNotNull { weatherEntity ->
                    weatherEntity?.convertToWeatherStoryModel()
                }.toMutableList()
                Observable.just(
                    DataResponseStatus.Success<MutableList<WeatherStoryModel>>(weatherStoriesList)
                )

            }
        } else {
            Observable.just(
                DataResponseStatus.Error<MutableList<WeatherStoryModel>>(
                    resourcesHelper.getStringRes(
                        R.string.error_getting_saved_weather_stories
                    )
                )
            )
        }
    }

    override fun removeStory(id: String): Observable<DataResponseStatus<Unit>> {
        dbHelper.deleteStory(id)
        return Observable.just(DataResponseStatus.Success<Unit>(Unit))
    }
}