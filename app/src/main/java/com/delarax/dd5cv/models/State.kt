package com.delarax.dd5cv.models

enum class State() {
    
}


data class State<out T>(
    val status: Status,
    val data: T?,
    val throwable: Throwable?
) {
    companion object {
        fun <T> success(data: T?): State<T> {

        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}