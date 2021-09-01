package com.example.robustatask.ui.main_activity

import com.example.robustatask.base.BasePresenter
import com.example.robustatask.domain.pojos.models.WeatherStoryModel

class MainActivityPresenter : BasePresenter<MainActivityContract.View>(),
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
        TODO("Not yet implemented")
    }

    override fun checkLoadedHistoryList(data: MutableList<WeatherStoryModel>?) {
        if (data.isNullOrEmpty()) {
            view?.onHistoryEmpty()
        } else {
            view?.onHistoryNotEmpty(data)
        }
    }

    override fun deleteStory(position: Int, storyID: String) {
        TODO("Not yet implemented")
    }

}