package com.feup.jtp.client_app.domain.repository

import com.feup.jtp.client_app.domain.model.User

interface UserRepository {

    suspend fun registerUser(user: User)
}