package com.acme.supermarket.server.service

import com.acme.supermarket.server.domain.User
import com.acme.supermarket.server.dto.RegisterUserRequestDto
import com.acme.supermarket.server.dto.UserResponseDto
import com.acme.supermarket.server.repository.UserRepository
import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import java.nio.file.Files
import java.nio.file.Paths

@Service
class UserService (
    private val userRepository: UserRepository
) {
    private val supermarketRsaPublicKey: String = loadRsaPublicKey()

    @Transactional
    fun updateUserHistory(userId: String, totalValue: BigDecimal, accumulatedDiscount: BigDecimal) {
        userRepository.updateTotalSpent(userId, totalValue)
        userRepository.updateAccumulatedDiscount(userId, accumulatedDiscount)
    }

    fun registerUser(request: RegisterUserRequestDto): UserResponseDto {
        val userUuid = UUID.randomUUID().toString()
        if (request.name.isBlank()) throw BadRequestException("The name cannot be empty.")
        val user = User(
            userUuid = userUuid,
            name = request.name,
            nickname = request.nickname,
            rsaPublicKey = request.rsaPublicKey,
            ecPublicKey = request.ecPublicKey,
            cardType = request.paymentCardDto.type,
            cardNumber = request.paymentCardDto.number,
            cardExpirationDate = request.paymentCardDto.expirationDate
        )

        userRepository.save(user)

        return UserResponseDto(
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