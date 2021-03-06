package com.delarax.dd5cv.di

import android.content.Context
import androidx.room.Room
import com.delarax.dd5cv.BuildConfig
import com.delarax.dd5cv.data.database.AppDatabase
import com.delarax.dd5cv.retrofit.ServiceResponseAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    // 10.0.2.2 is how to connect to localhost of the computer. 127.0.0.1 would be the emulator.
    fun providesBaseUrl() = "http://10.0.2.2:3000" // TODO: make configurable


    @Provides
    fun providesOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ServiceResponseAdapterFactory())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    fun providesAppDatabase(
        @ApplicationContext applicationContext: Context
    ) : AppDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        "dd5cv-app-database"
    )
        .fallbackToDestructiveMigration() // TODO: this will delete all saved data when the db version changes. Remove this before actually using the app.
        .build()
}