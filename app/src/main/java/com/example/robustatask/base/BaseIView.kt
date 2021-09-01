package com.example.robustatask.base


interface BaseIView {
    fun showLoading() {}
    fun hideLoading() {}
    fun showError(err: String?) {}
    fun onPermissionGranted()
    fun onPermissionNotGranted()

}