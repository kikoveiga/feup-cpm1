package com.acme.supermarket.server.service

import com.acme.supermarket.server.domain.User
import com.acme.supermarket.server.dto.RegistrationRequest
import com.acme.supermarket.server.dto.UserResponse
import com.acme.supermarket.server.repository.UserRepository
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.util.*
import java.nio.file.Files
import java.nio.file.Paths

@Service
class UserService (
    private val userRepository: UserRepository
) {
    private val supermarketRsaPublicKey: String = loadRsaPublicKey()

    fun registerUser(request: RegistrationRequest): UserResponse {
        val userUuid = UUID.randomUUID().toString()
        if (request.name.isBlank()) throw BadRequestException("The name cannot be empty.")
        val user = User(
            userUuid = userUuid,
            name = request.name,
            nickname = request.nickname,
            rsaPublicKey = request.rsaPublicKey,
            ecdsaPublicKey = request.ecdsaPublicKey,
            cardType = request.cardType,
            cardNumber = request.cardNumber,
            cardExpirationDate = request.cardExpirationDate
        )

        userRepository.save(user)

        return UserResponse(
            userUuid = userUuid,
            supermarketRsaPublicKey = supermarketRsaPublicKey
        )
    }

    fun findByUserUuid(userUuid: String): User? {
        return userRepository.findByUserUuid(userUuid)
    }

    private fun loadRsaPublicKey(): String {
        val path = Paths.get("src/main/resources/keys/supermarket-public.pem")
        return Files.readString(path)
    }
}