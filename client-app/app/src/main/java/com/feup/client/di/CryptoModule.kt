package com.feup.client.di

import com.feup.client.data.crypto.CryptoManagerImpl
import com.feup.client.domain.crypto.CryptoManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CryptoModule {

    @Binds
    @Singleton
    abstract fun bindCryptoManager(
        cryptoManagerImpl: CryptoManagerImpl
    ): CryptoManager
}