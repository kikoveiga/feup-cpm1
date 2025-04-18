package com.feup.client.di

import com.feup.client.domain.local.UserDataStore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UserDataStoreEntryPoint {
    fun userDataStore(): UserDataStore
}