package com.feup.jtp.client_app.domain.local

interface UserPreferences {
    suspend fun setUserRegistered(userId: String)
    suspend fun isRegistered(): Boolean
    suspend fun getUserId(): String?
}