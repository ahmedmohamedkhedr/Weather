package com.example.robustatask.di

import com.example.robustatask.domain.useCases.WeatherUseCase
import com.example.robustatask.domain.useCases.WeatherUseCaseImp
import org.koin.dsl.module

val domainModule = module {
    single<WeatherUseCase> {
        WeatherUseCaseImp(get(), get(), get())
    }
}