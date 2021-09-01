package com.example.robustatask.ui.preview_image

import android.content.Intent
import android.location.Location
import com.example.robustatask.base.BasePresenter
import com.example.robustatask.base.DataResponseStatus
import com.example.robustatask.domain.useCases.WeatherUseCase
import com.example.robustatask.ui.main_activity.MainActivity.Companion.ARG_IMAGE_FILE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PreviewActivityPresenter(private val useCase: WeatherUseCase) :
    BasePresenter<PreviewActivityContract.View>(),
    PreviewActivityContract.Presenter {

    override fun getLatLon(location: Location?) {
        location?.let {
            view?.onGetLatLon(it.latitude, it.longitude)
        }
    }

    override fun getActivityIntent(intent: Intent?) {
        intent?.apply {
            getStringExtra(ARG_IMAGE_FILE)?.let {
                view?.onFetchActivityArgs(it)
            }
        }
    }

    override fun loadWeatherDetails(lat: Double, lon: Double) {
        view?.showLoading()
        compositeDisposable.add(
            useCase.getWeatherDetails(lat, lon).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    when (it) {
                        is DataResponseStatus.Success -> {
                            view?.onLoadWeatherDetailsSuccess(it.response)
                        }
                        is DataResponseStatus.Error -> {
                            view?.showError(it.errorMessage)
                        }
                    }
                }, {
                    view?.hideLoading()
                    view?.showError(it.message)
                })
        )
    }
}