package com.example.robustatask.ui.preview_image

import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import com.example.robustatask.base.BaseIPresenter
import com.example.robustatask.base.BaseIView
import com.example.robustatask.domain.pojos.models.WeatherModel

interface PreviewActivityContract {

    interface Presenter : BaseIPresenter {
        fun getLatLon(location: Location?)
        fun getActivityIntent(intent: Intent?)
        fun loadWeatherDetails(lat: Double, lon: Double)
    }

    interface View : BaseIView {
        fun onFetchActivityArgs(imagePath: String)
        fun onLoadWeatherDetailsSuccess(weather: WeatherModel)
        fun onGetLatLon(lat: Double, lon: Double)
    }
}