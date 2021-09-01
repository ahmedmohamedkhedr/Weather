package com.example.robustatask.ui.main_activity

import com.example.robustatask.base.BaseIPresenter
import com.example.robustatask.base.BaseIView
import com.example.robustatask.domain.pojos.models.WeatherStoryModel

interface MainActivityContract {

    interface Presenter : BaseIPresenter {
        fun setPickedImagePath(path: String?)
        fun checkDataFromActivityResult()
        fun loadHistory()
        fun checkLoadedHistoryList(data: MutableList<WeatherStoryModel>?)
        fun deleteStory(position: Int, storyID: String)
    }

    interface View : BaseIView {
        fun onPickImageSuccess(imagePath: String)
        fun onPickImageError()
        fun onLoadHistorySuccess(historyStories: MutableList<WeatherStoryModel>?)
        fun onDeleteStorySuccess(position: Int)
        fun onHistoryEmpty()
        fun onHistoryNotEmpty(history: MutableList<WeatherStoryModel>)
    }
}