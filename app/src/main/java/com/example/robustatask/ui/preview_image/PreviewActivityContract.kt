package com.example.robustatask.ui.preview_image

import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import com.example.robustatask.base.BaseIPresenter
import com.example.robustatask.base.BaseIView
import com.example.robustatask.domain.pojos.models.WeatherModel
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import java.io.File

interface PreviewActivityContract {

    interface Presenter : BaseIPresenter {
        fun getLatLon(location: Location?)
        fun getActivityIntent(intent: Intent?)
        fun loadWeatherDetails(lat: Double, lon: Double)
        fun shareStoryToFacebook(file: File)
        fun shareStoryToTwitter(file: File)
        fun prepareSavingStory(weather: WeatherModel, storyFile: File)
        fun saveWeatherStory(weatherStory: WeatherStoryModel)
        fun publishHistoryEvent()
    }

    interface View : BaseIView {
        fun onPreviewEntrance(imagePath: String)
        fun onNotPreviewEntrance(imagePath: String)
        fun onLoadWeatherDetailsSuccess(weather: WeatherModel)
        fun onGetLatLon(lat: Double, lon: Double)
        fun onPrepareFacebookSharingSuccess(storyPath: String)
        fun onPrepareTwitterSharingSuccess(storyPath: String)
        fun onPrepareSavingStorySuccess(weatherStory: WeatherStoryModel)
        fun onSaveWeatherStorySuccess()

    }
}