package com.feup.client.domain.local

import com.feup.client.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.security.PublicKey

interface UserDataStore {
    val isLoggedInFlow: Flow<Boolean>
    suspend fun doesUserExist(nickname: String): Boolean
    suspend fun saveUser(user: User)
    suspend fun login(nickname: String, password: String)
    suspend fun logoutUser()
    suspend fun getLoggedInUser(): User
    suspend fun getSupermarketRsaPublicKey(): PublicKey
}