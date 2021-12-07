package com.delarax.dd5cv.data.di

import com.delarax.dd5cv.data.characters.local.LocalCharacterDataSource
import com.delarax.dd5cv.data.characters.local.LocalCharacterDataSourceMocked
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSource
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataSourceModule::class]
)
internal abstract class TestDataSourceModule {
    @Binds
    abstract fun bindsRemoteCharacterDataSource(
        remoteCharacterDataSource: RemoteCharacterDataSourceMocked
    ): RemoteCharacterDataSource

    @Binds
    abstract fun bindsLocalCharacterDataSource(
        localCharacterDataSource: LocalCharacterDataSourceMocked
    ): LocalCharacterDataSource
}