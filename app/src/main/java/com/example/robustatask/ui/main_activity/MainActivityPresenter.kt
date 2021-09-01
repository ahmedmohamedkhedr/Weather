package com.example.robustatask.ui.main_activity

import com.example.robustatask.base.BasePresenter

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

}