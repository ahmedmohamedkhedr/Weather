package com.example.robustatask.base

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<View : BaseIView> : LifecycleObserver, BaseIPresenter {

    var view: View? = null
    var compositeDisposable: CompositeDisposable = CompositeDisposable()


    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun attachView(view: Any, viewLifecycle: Lifecycle?) {
        this.view = view as? View
        viewLifecycle?.addObserver(this)
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    @CallSuper
    override fun dispose() {
        compositeDisposable.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @CallSuper
    override fun detachView() {
        view = null
    }

    override fun isViewAttached() = view != null

}