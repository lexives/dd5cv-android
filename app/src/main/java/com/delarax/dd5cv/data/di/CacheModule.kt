package com.delarax.dd5cv.data.di

import com.delarax.dd5cv.data.characters.CharacterCache
import com.delarax.dd5cv.data.characters.CharacterCacheImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class CacheModule {

    @Provides
    fun providesCharacterCache(
        characterCache: CharacterCacheImpl
    ): CharacterCache = characterCache
}