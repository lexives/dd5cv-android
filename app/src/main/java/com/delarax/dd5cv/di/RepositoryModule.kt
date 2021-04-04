package com.delarax.dd5cv.di

import com.delarax.dd5cv.data.CharacterRepo
import com.delarax.dd5cv.data.CharacterRepoMockData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsCharacterRepo(
        characterRepoImpl: CharacterRepoMockData
    ): CharacterRepo
}