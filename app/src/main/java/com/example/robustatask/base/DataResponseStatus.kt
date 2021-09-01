package com.example.robustatask.base

sealed class DataResponseStatus<T> {
    data class Success<T>(val response: T) : DataResponseStatus<T>()
    data class Error<T>(val errorMessage: String) : DataResponseStatus<T>()
}