package com.feup.client.domain.repository

import com.feup.client.domain.model.User

interface UserRepository {
    suspend fun registerUser(user: User): Result<User>
}