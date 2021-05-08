package com.delarax.dd5cv.utils.retrofit

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.lang.UnsupportedOperationException

/**
 * An implementation of the `Call` interface for Retrofit. Contains the logic for how to
 * convert Retrofit's default `Call` object into our custom `ServiceResponse` class.
 *
 * Generics:
 *   - S = Success: the type of the object that is returned upon success
 *   - E = Error: the type of the object that is returned upon a "successful" error,
 *          such as a 404.
 */
internal class ServiceResponseCall<S: Any, E: Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<ServiceResponse<S, E>> {

    override fun enqueue(callback: Callback<ServiceResponse<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    body?.let {
                        callback.onResponse(
                            this@ServiceResponseCall,
                            Response.success(ServiceResponse.ServiceSuccess(body))
                        )
                    } ?: callback.onResponse(
                        this@ServiceResponseCall,
                        Response.success(ServiceResponse.UnknownError(
                            Throwable("Response was successful but body is null")
                        ))
                    )
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }
                    errorBody?.let {
                        callback.onResponse(
                            this@ServiceResponseCall,
                            Response.success(ServiceResponse.ServiceError(errorBody, code))
                        )
                    } ?: callback.onResponse(
                        this@ServiceResponseCall,
                        Response.success(ServiceResponse.UnknownError(
                            Throwable(
                                "Response was not successful and error body was null or empty"
                            )
                        ))
                    )
                }
            }

            override fun onFailure(call: Call<S>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> ServiceResponse.NetworkError(throwable)
                    else -> ServiceResponse.UnknownError(throwable)
                }
                callback.onResponse(
                    this@ServiceResponseCall,
                    Response.success(networkResponse)
                )
            }
        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun clone(): Call<ServiceResponse<S, E>> =
        ServiceResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<ServiceResponse<S, E>> {
        throw UnsupportedOperationException("${this::class.simpleName} doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}