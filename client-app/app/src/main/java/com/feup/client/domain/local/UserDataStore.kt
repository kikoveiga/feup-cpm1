package com.feup.client.domain.local

import com.feup.client.domain.model.User
import java.security.PublicKey

interface UserDataStore {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User
    suspend fun getSupermarketRsaPublicKey(): PublicKey
    suspend fun isRegistered(): Boolean
}