package com.feup.jtp.client_app.domain.interactor.auth

import com.feup.jtp.client_app.domain.crypto.CryptoManager
import com.feup.jtp.client_app.domain.local.UserPreferences
import com.feup.jtp.client_app.domain.model.PaymentCard
import com.feup.jtp.client_app.domain.model.User
import com.feup.jtp.client_app.domain.repository.UserRepository
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
                paymentCard = paymentCard,
                rsaKeyPair = cryptoManager.generateRSAKeyPair(),
                ecKeyPair = cryptoManager.generateECKeyPair()
            )

            val result = userRepository.registerUser(user)

            if (result.isSuccess) {
                val registeredUser = result.getOrThrow()

                if (registeredUser.uuid == null) {
                    Result.failure<Exception>(Exception("UUID is null in registered user"))
                } else {
                    userPreferences.setUserRegistered(registeredUser.uuid)
                    Result.success(Unit)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}