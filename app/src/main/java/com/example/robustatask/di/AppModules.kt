package com.example.robustatask.di

import com.example.robustatask.ui.main_activity.MainActivityContract
import com.example.robustatask.ui.main_activity.MainActivityPresenter
import com.example.robustatask.ui.preview_image.PreviewActivityContract
import com.example.robustatask.ui.preview_image.PreviewActivityPresenter
import com.example.robustatask.utils.BASE_URL
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModules = module {

    factory<MainActivityContract.Presenter> {
        MainActivityPresenter()
    }

    factory<PreviewActivityContract.Presenter> {
        PreviewActivityPresenter(get())
    }
}