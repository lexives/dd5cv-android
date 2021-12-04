package com.delarax.dd5cv.models

sealed class State<T> {
    data class Success<T>(val value: T) : State<T>()
    data class Error<T>(val throwable: Throwable, val statusCode: Int? = null) : State<T>()
    data class Loading<T>(val progress: Int = 0) : State<T>()
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

    /**
     * Transforms the object within the Success and Empty states.
     *
     * @param mapper: The function that will be applied to the state's contents.
     */
    fun <R> mapSuccess(mapper: (T) -> R) : State<R> {
        return when (this) {
            is Success -> Success(mapper(this.value))
            is Error -> Error(this.throwable, this.statusCode)
            is Loading -> Loading(this.progress)
            is Empty -> Empty(mapper(this.default))
        }
    }

    /**
     * Transforms the Success and Empty states into another state. Commonly used for
     * converting Success into Empty.
     *
     * @param mapper: The function that will be applied to the state
     */
    fun <R> flatMapSuccess(mapper: (T) -> State<R>) : State<R> {
        return when (this) {
            is Success -> mapper(this.value)
            is Error -> Error(this.throwable, this.statusCode)
            is Loading -> Loading(this.progress)
            is Empty -> mapper(this.default)
        }
    }
}