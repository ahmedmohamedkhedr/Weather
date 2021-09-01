package com.example.robustatask.di

import com.example.robustatask.data.apiHelper.ApiHelper
import com.example.robustatask.data.apiHelper.ApiHelperImp
import com.example.robustatask.data.network.ApiService
import com.example.robustatask.data.resourcesHelper.ResourcesHelper
import com.example.robustatask.data.resourcesHelper.ResourcesHelperImp
import com.example.robustatask.utils.BASE_URL
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val RETROFIT_CLIENT = "RETROFIT_CLIENT"
private const val API_SERVICE = "API_SERVICE"

private fun getRetrofit(): Retrofit {
    val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS).build()

    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
}


val dataModule = module {
    single(named(RETROFIT_CLIENT)) {
        getRetrofit()
    }

    single<ApiService>(named(API_SERVICE)) {
        get<Retrofit>(named(RETROFIT_CLIENT)).create(ApiService::class.java)
    }

    single<ResourcesHelper> {
        ResourcesHelperImp(androidContext())
    }

    single<ApiHelper> {
        ApiHelperImp(get(named(API_SERVICE)))
    }
}