package com.example.robustatask.ui.main_activity

import com.example.robustatask.base.BaseIPresenter
import com.example.robustatask.base.BaseIView

interface MainActivityContract {

    interface Presenter : BaseIPresenter {
        fun setPickedImagePath(path:String?)
        fun checkDataFromActivityResult()
    }

    interface View : BaseIView {
        fun onPickImageSuccess(imagePath:String)
        fun onPickImageError()
    }
}