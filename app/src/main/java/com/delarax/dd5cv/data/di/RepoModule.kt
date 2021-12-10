package com.delarax.dd5cv.data.di

import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.data.characters.CharacterRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class RepoModule {

    @Provides
    fun providesCharacterRepo(
        characterRepo: CharacterRepoImpl
    ): CharacterRepo = characterRepo
}