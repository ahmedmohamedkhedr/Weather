package com.example.robustatask.ui.preview_image

import android.content.Intent
import android.location.Location
import com.example.robustatask.base.BasePresenter
import com.example.robustatask.base.DataResponseStatus
import com.example.robustatask.base.convertToWeatherStoryModel
import com.example.robustatask.domain.pojos.models.WeatherModel
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import com.example.robustatask.domain.useCases.WeatherUseCase
import com.example.robustatask.ui.preview_image.PreviewActivity.Companion.ARG_ENTRANCE_TYPE
import com.example.robustatask.ui.preview_image.PreviewActivity.Companion.EntranceType
import com.example.robustatask.ui.preview_image.PreviewActivity.Companion.ARG_IMAGE_FILE
import com.example.robustatask.utils.RxBus
import com.example.robustatask.utils.RxEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class PreviewActivityPresenter(private val useCase: WeatherUseCase) :
    BasePresenter<PreviewActivityContract.View>(),
    PreviewActivityContract.Presenter {
    private var imagePath: String? = null

    override fun getLatLon(location: Location?) {
        location?.let {
            view?.onGetLatLon(it.latitude, it.longitude)
        }
    }

    override fun getActivityIntent(intent: Intent?) {
        imagePath = intent?.getStringExtra(ARG_IMAGE_FILE)
        val entranceType: EntranceType? =
            intent?.getSerializableExtra(ARG_ENTRANCE_TYPE) as EntranceType

        if (entranceType == null || entranceType == EntranceType.PREVIEW_TYPE) {
            imagePath?.let { view?.onPreviewEntrance(it) }
        } else {
            imagePath?.let { view?.onNotPreviewEntrance(it) }
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

    override fun shareStoryToFacebook(file: File) {
        view?.onPrepareFacebookSharingSuccess(file.path)
    }

    override fun shareStoryToTwitter(file: File) {
        view?.onPrepareTwitterSharingSuccess(file.path)
    }

    override fun prepareSavingStory(weather: WeatherModel, storyFile: File) {
        view?.onPrepareSavingStorySuccess(weather.convertToWeatherStoryModel(storyFile.path))
    }

    override fun saveWeatherStory(weatherStory: WeatherStoryModel) {
        compositeDisposable.add(
            useCase.saveWeatherStory(weatherStory).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is DataResponseStatus.Success -> {
                            view?.onSaveWeatherStorySuccess()
                        }
                        is DataResponseStatus.Error -> {
                            view?.showError(it.errorMessage)
                        }
                    }
                }, {
                    view?.showError(it.message)
                })
        )
    }

    override fun publishHistoryEvent() {
        RxBus.publish(RxEvent.OnStoryAdded())
    }

}