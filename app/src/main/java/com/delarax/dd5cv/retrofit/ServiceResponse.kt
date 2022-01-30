package com.delarax.dd5cv.retrofit

import com.delarax.dd5cv.models.data.ErrorModel
import com.delarax.dd5cv.models.data.State
import java.io.IOException

/**
 * A class that represents the possible outcomes of a Retrofit call.
 *
 * Generics:
 *   - S = Success: the type of the object that is returned upon success
 *   - E = Error: the type of the object that is returned upon a "successful" error,
 *          such as a 404.
 *
 * @see ServiceResponseAdapterFactory for how this in used with Retrofit to transform
 *      the regular `Call` object into a `ServiceResponse`.
 *
 * @example
 * An endpoint returns this body upon success (let's call it User):
 * <pre>
 * {
 *  "name" : "Ash",
 *  "age" : "50 years"
 * }
 * </pre>
 *
 * and this body upon error (let's call it ErrorModel):
 * <pre>
 * {
 *  "code" : 404,
 *  "message" : "not found"
 * }
 * </pre>
 *
 * You can set up your Retrofit call like this:
 * <pre>{@code
 * @GET("users/{id}")
 * suspend fun getUser(@Path("id") id: String): ServiceResponse<User, ErrorModel>
 * }</pre>
 */
sealed class ServiceResponse<out S : Any, out E : Any> {
    /**
     * Success response with body of type `S` (success)
     */
    data class ServiceSuccess<T : Any>(val body: T) : ServiceResponse<T, Nothing>()

    /**
     * Failure response with body of type `E` (error), as well as a status code
     */
    data class ServiceError<U : Any>(val body: U, val code: Int? = null) : ServiceResponse<Nothing, U>()

    /**
     * Network error with no body, just an IOException
     */
    data class NetworkError(val error: IOException) : ServiceResponse<Nothing, Nothing>()

    /**
     * Unknown error with no body, just a generic Throwable.
     * For example, a json parsing error.
     */
    data class UnknownError(val error: Throwable) : ServiceResponse<Nothing, Nothing>()
}

/**
 * Maps a ServiceResponse object to a State object.
 */
fun <S : Any, E : Any> ServiceResponse<S, E>.mapToState() : State<S> =
    this.mapToStateAndTransform { it }

/**
 * Maps a ServiceResponse object to a State object, and transforms the object within
 * the state into another type using State.mapSuccess().
 *
 * @param mapper    A function that turns the success type (S) into a new type (T)
 */
fun <S : Any, E : Any, T> ServiceResponse<S, E>.mapToStateAndTransform(
    mapper: (S) -> T
) : State<T> {
    return when (this) {
        is ServiceResponse.ServiceSuccess -> {
            State.Success(this.body).mapSuccess(mapper)
        }
        is ServiceResponse.ServiceError -> {
            if (this.body is ErrorModel) {
                State.Error(
                    throwable = Throwable(this.body.error),
                    statusCode = this.code
                )
            } else {
                State.Error(Throwable(this.body.toString()))
            }
        }
        is ServiceResponse.NetworkError -> {
            State.Error(this.error)
        }
        is ServiceResponse.UnknownError -> {
            State.Error(this.error)
        }
        else -> {
            State.Error(Throwable("Unknown Error when mapping from ServiceResponse to State"))
        }
    }
}
