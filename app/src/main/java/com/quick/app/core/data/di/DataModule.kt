package com.quick.app.core.data.di

import com.quick.app.core.data.repository.LocalUserDataRepository
import com.quick.app.core.data.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    internal abstract fun bindsUserDataRepository(
        userDataRepository: LocalUserDataRepository,
    ): UserDataRepository
}