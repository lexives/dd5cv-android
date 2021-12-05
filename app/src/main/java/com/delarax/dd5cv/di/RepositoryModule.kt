package com.delarax.dd5cv.di

import com.delarax.dd5cv.data.characters.repo.CharacterDatabaseRepo
import com.delarax.dd5cv.data.characters.repo.CharacterRepo
import com.delarax.dd5cv.data.characters.repo.CharacterRepoMockData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsCharacterRepo(
        characterRepoImpl: CharacterRepoMockData // TODO: make configurable
    ): CharacterRepo

    @Binds
    abstract fun bindsCharacterDatabaseRepo(
        characterDatabaseRepo: CharacterDatabaseRepo
    ): CharacterDatabaseRepo
}