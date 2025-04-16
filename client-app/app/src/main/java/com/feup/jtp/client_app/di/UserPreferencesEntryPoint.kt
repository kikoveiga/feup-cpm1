package com.feup.jtp.client_app.di

import com.feup.jtp.client_app.domain.local.UserPreferences
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UserPreferencesEntryPoint {
    fun userPreferences(): UserPreferences
}