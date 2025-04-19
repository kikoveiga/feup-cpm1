package com.feup.client.di

import com.feup.client.data.local.datastore.UserDataStoreImpl
import com.feup.client.domain.local.UserDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserDataStoreModule {

    @Binds
    @Singleton
    abstract fun bindUserDataStore(
        userDataStoreImpl: UserDataStoreImpl
    ): UserDataStore
}