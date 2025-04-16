package com.feup.client.domain.local

import com.feup.client.domain.model.User

interface UserPreferences {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User
    suspend fun isRegistered(): Boolean
}