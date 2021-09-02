package com.example.robustatask.ui.main_activity

import android.annotation.SuppressLint
import com.example.robustatask.base.BasePresenter
import com.example.robustatask.base.DataResponseStatus
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import com.example.robustatask.domain.useCases.WeatherUseCase
import com.example.robustatask.utils.RxBus
import com.example.robustatask.utils.RxEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivityPresenter(private val useCase: WeatherUseCase) :
    BasePresenter<MainActivityContract.View>(),
    MainActivityContract.Presenter {
    private var pickedPhotoPath: String? = null

    override fun setPickedImagePath(path: String?) {
        this.pickedPhotoPath = path
    }

    override fun checkDataFromActivityResult() {
        if (pickedPhotoPath != null) {
            view?.onPickImageSuccess(pickedPhotoPath!!)
        } else {
            view?.onPickImageError()
        }
    }

    override fun loadHistory() {
        compositeDisposable.add(
            useCase.getHistory().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is DataResponseStatus.Success -> {
                            view?.onLoadHistorySuccess(it.response)
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

    @SuppressLint("CheckResult")
    override fun listenToHistoryUpdates() {
        RxBus.listen(RxEvent.OnStoryAdded::class.java).subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                loadHistory()
            }
    }

    override fun checkLoadedHistoryList(data: MutableList<WeatherStoryModel>?) {
        if (data.isNullOrEmpty()) {
            view?.onHistoryEmpty()
        } else {
            view?.onHistoryNotEmpty(data)
        }
    }

    override fun deleteStory(position: Int, storyID: String) {
        compositeDisposable.add(
            useCase.removeStory(storyID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is DataResponseStatus.Success -> {
                            view?.onDeleteStorySuccess(position)
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

}