package com.delarax.dd5cv.data.di

import com.delarax.dd5cv.data.characters.local.LocalCharacterDataSource
import com.delarax.dd5cv.data.characters.local.LocalCharacterDataSourceRoom
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSource
import com.delarax.dd5cv.data.characters.remote.RemoteCharacterDataSourceMocked
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {

    @Binds
    abstract fun bindsRemoteCharacterDataSource(
        remoteCharacterDataSource: RemoteCharacterDataSourceMocked // TODO: make configurable
    ): RemoteCharacterDataSource

    @Binds
    abstract fun bindsLocalCharacterDataSource(
        localCharacterDataSource: LocalCharacterDataSourceRoom
    ): LocalCharacterDataSource
}