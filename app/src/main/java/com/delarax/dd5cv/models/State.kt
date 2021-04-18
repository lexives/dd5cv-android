package com.delarax.dd5cv.models

sealed class State<T>() {
    data class Success<T>(val value: T) : State<T>()
    data class Error<T>(val throwable: Throwable) : State<T>()
    data class Loading<T>(val progress: Int) : State<T>()
    data class Empty<T>(val default: T) : State<T>()

    /**
     * Gets the data from a state, returning a null value
     * if the current state doesn't have data.
     */
    fun getOrNull(): T? {
        return when (this) {
            is Success -> this.value
            is Empty -> this.default
            else -> null
        }
    }

    /**
     * Gets the data from a state, returning the default value
     * if the current state doesn't have data.
     *
     * @param default: The value returned if the state doesn't have data
     */
    fun getOrDefault(default: T): T {
        return when (this) {
            is Success -> this.value
            is Empty -> this.default
            else -> default
        }
    }
}