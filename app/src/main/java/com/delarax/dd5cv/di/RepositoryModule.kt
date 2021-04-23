package com.delarax.dd5cv.di

import com.delarax.dd5cv.data.characters.CharacterRepo
import com.delarax.dd5cv.data.characters.CharacterRepoApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsCharacterRepo(
        characterRepoImpl: CharacterRepoApi
    ): CharacterRepo
}