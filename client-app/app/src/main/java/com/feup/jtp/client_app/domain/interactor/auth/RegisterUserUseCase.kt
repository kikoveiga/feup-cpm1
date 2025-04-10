package com.feup.jtp.client_app.domain.interactor.auth

import com.feup.jtp.client_app.domain.crypto.CryptoManager
import com.feup.jtp.client_app.domain.model.PaymentCard
import com.feup.jtp.client_app.domain.model.User
import com.feup.jtp.client_app.domain.repository.UserRepository
import java.util.UUID

class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val cryptoManager: CryptoManager
) {
    suspend operator fun invoke(
        name: String,
        nickname: String,
        paymentCard: PaymentCard
    ): Result<User> {
        return try {
            val rsaKeyPair = cryptoManager.generateRSAKeyPair()
            val ecKeyPair = cryptoManager.generateECKeyPair()

            val userId = UUID.randomUUID().toString()
            val user = User(
                id = userId,
                name = name,
                paymentCard = paymentCard,
                publicRSAKey = rsaKeyPair.toString(),
                publicECKey = ecKeyPair.toString()
            )

            userRepository.registerUser(user)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}