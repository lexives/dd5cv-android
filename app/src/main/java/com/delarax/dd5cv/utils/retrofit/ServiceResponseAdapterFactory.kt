package com.delarax.dd5cv.utils.retrofit

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * A Retrofit `CallAdapter.Factory()`, which contains the logic and error handling for
 * checking if the return type of your Retrofit call can be converted with our `CallAdapter`.
 *
 * This is given directly to our Retrofit instance using `.addCallAdapterFactory()`
 * @see com.delarax.dd5cv.di.AppModule.providesRetrofit
 *
 * Generics:
 *   - S = Success: the type of the object that is returned upon success
 *   - E = Error: the type of the object that is returned upon a "successful" error,
 *          such as a 404.
 *
 * @see ServiceResponseAdapter
 */
class ServiceResponseAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in 'Call'
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is 'ParameterizedType'
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<ServiceResponse<Foo>>" +
                    " or Call<ServiceResponse<out Foo>>"
        }

        // get the response type inside 'Call' type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not ServiceResponse then we can't handle this type
        if (getRawType(responseType) != ServiceResponse::class.java) {
            return null
        }

        // the response type is ServiceResponse and should be parameterized
        check(responseType is ParameterizedType) {
            "response must be parameterized as ServiceResponse<Foo> or ServiceResponse<out Foo>"
        }

        val successBodyType = getParameterUpperBound(0, responseType)
        val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter =
            retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

        return ServiceResponseAdapter<Any, Any>(successBodyType, errorBodyConverter)
    }
}