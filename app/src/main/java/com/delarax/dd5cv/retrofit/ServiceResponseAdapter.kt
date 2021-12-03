package com.delarax.dd5cv.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * An implementation of the `CallAdapter` interface for Retrofit, which is used by our
 * Retrofit instance to automatically convert its default `Call` objects into our custom
 * `ServiceResponse` class.
 *
 * Generics:
 *   - S = Success: the type of the object that is returned upon success
 *   - E = Error: the type of the object that is returned upon a "successful" error,
 *          such as a 404.
 *
 * @see ServiceResponseAdapterFactory
 */
class ServiceResponseAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<ServiceResponse<S, E>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<ServiceResponse<S, E>> =
        ServiceResponseCall(call, errorBodyConverter)
}