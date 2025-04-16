package com.feup.client.domain.interactor.auth

import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.local.UserPreferences
import com.feup.client.domain.model.PaymentCard
import com.feup.client.domain.model.User
import com.feup.client.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val cryptoManager: CryptoManager,
    private val userPreferences: UserPreferences
) {
    suspend operator fun invoke(
        name: String,
        nickname: String,
        paymentCard: PaymentCard
    ): Result<Unit> {
        return try {
            val user = User(
                name = name,
                nickname = nickname,
                rsaKeyPair = cryptoManager.generateRSAKeyPair(),
                ecKeyPair = cryptoManager.generateECKeyPair(),
                paymentCard = paymentCard
            )

            val result = userRepository.registerUser(user)

            if (result.isSuccess) {
                val registeredUser = result.getOrThrow()

                if (registeredUser.uuid == null) {
                    return Result.failure(Exception("UUID is null in registered user"))
                }

                userPreferences.saveUser(registeredUser)
                return Result.success(Unit)
            }

            return Result.failure(result.exceptionOrNull() ?: Exception("Failed to register user"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}