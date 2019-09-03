package com.cezma.app.utiles


sealed class DataResource<out T : Any> {
    data class Success<out T : Any>(val data: T) : DataResource<T>()
    data class Error(val message: String) : DataResource<Nothing>()
}