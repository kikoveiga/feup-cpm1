package com.acme.supermarket.server.service

import com.acme.supermarket.server.domain.User
import com.acme.supermarket.server.dto.RegistrationRequest
import com.acme.supermarket.server.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService (
    private val userRepository: UserRepository
) {
    fun registerUser(request: RegistrationRequest): User {
        val userUuid = UUID.randomUUID().toString()

        val user = User(
            userUuid = userUuid,
            name = request.name,
            rsaPublicKey = request.rsaPublicKey,
            ecdsaPublicKey = request.ecdsaPublicKey
        )

        return userRepository.save(user)
    }

    fun findByUserUuid(userUuid: String): User? {
        return userRepository.findByUserUuid(userUuid)
    }
}