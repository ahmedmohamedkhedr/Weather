package com.example.robustatask.base

import androidx.lifecycle.Lifecycle

interface BaseIPresenter {
    fun attachView(view: Any, viewLifecycle: Lifecycle?)
    fun detachView()
    fun isViewAttached(): Boolean
}