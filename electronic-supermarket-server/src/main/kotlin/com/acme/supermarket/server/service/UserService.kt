package com.acme.supermarket.server.service

import com.acme.supermarket.server.domain.CardType
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

        validateRequest(request)
        val cleanedCardNumber = request.paymentCardDto.number.replace(Regex("[^\\d]"), "")

        val user = User(
            userUuid = userUuid,
            name = request.name,
            nickname = request.nickname,
            rsaPublicKey = request.rsaPublicKey,
            ecPublicKey = request.ecPublicKey,
            cardType = request.paymentCardDto.type,
            cardNumber = cleanedCardNumber,
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

    private fun validateRequest(request: RegisterUserRequestDto) {

        //If the requests are blank
        if (request.name.isBlank()) throw BadRequestException("The name cannot be empty.")
        if (request.nickname.isBlank()) throw BadRequestException("The nickname cannot be empty.")
        if (request.rsaPublicKey.isBlank()) throw BadRequestException("The rsaPublicKey cannot be empty.")
        if (request.ecPublicKey.isBlank()) throw BadRequestException("The ecPublicKey cannot be empty.")
        if (request.paymentCardDto.type.isBlank()) throw BadRequestException("The type cannot be empty.")
        if (request.paymentCardDto.number.isBlank()) throw BadRequestException("Card number cannot be empty.")
        if (request.paymentCardDto.expirationDate.isBlank()) throw BadRequestException("Expiration date cannot be empty.")

        try {
            CardType.valueOf(request.paymentCardDto.type.uppercase())
        } catch (e: IllegalArgumentException) {
            throw BadRequestException("Invalid card type. Allowed values are 'DEBIT' or 'CREDIT'.")
        }
        if (!isValidCardNumber(request.paymentCardDto.number)) throw BadRequestException("Invalid card number.")
        if (!isValidCardExpirationDate(request.paymentCardDto.expirationDate)) throw BadRequestException("Invalid expiration date.")

    }

    private fun isValidCardExpirationDate(expirationDate: String): Boolean {
        val pattern = Regex("^(0[1-9]|1[0-2])/([0-9]{2})$")
        if (!expirationDate.matches(pattern)) return false

        val (monthStr, yearStr) = expirationDate.split("/")
        val month = monthStr.toInt()
        val year = yearStr.toInt()

        if (month !in 1..12) return false

        val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
        if (year < currentYear) return false
        if (year == currentYear && month < Calendar.getInstance().get(Calendar.MONTH)) return false

        return true
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        val cleanedNumber = cardNumber.replace(" ", "")
        return cleanedNumber.length in 13..19 && isValidLuhn(cleanedNumber)
    }

    private fun isValidLuhn(cardNumber: String): Boolean {
        var sum = 0
        var shouldDouble = false
        for (i in cardNumber.length - 1 downTo 0) {
            var digit = cardNumber[i].toString().toInt()
            if (shouldDouble) {
                digit *= 2
                if (digit > 9) digit -= 9
            }
            sum += digit
            shouldDouble = !shouldDouble
        }
        return sum % 10 == 0
    }

}