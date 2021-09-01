package com.example.robustatask

import android.app.Application
import com.example.robustatask.di.appModules
import com.example.robustatask.di.dataModule
import com.example.robustatask.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModules, dataModule, domainModule))
        }
    }
}