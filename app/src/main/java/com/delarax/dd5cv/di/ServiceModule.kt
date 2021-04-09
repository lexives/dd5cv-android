package com.delarax.dd5cv.di

import com.delarax.dd5cv.data.characters.api.CharacterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class ServiceModule {

    @Provides
    fun providesCharacterService(
        retrofit: Retrofit
    ): CharacterService = retrofit.create(CharacterService::class.java)
}