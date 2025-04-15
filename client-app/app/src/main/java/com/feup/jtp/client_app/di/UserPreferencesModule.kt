package com.feup.jtp.client_app.di

import com.feup.jtp.client_app.data.local.UserPreferencesImpl
import com.feup.jtp.client_app.domain.local.UserPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPreferencesModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferences(
        userPreferencesImpl: UserPreferencesImpl
    ): UserPreferences
}