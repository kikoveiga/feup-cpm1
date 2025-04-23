package com.feup.client.domain.usecases

import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.model.PaymentCard
import com.feup.client.domain.model.User
import com.feup.client.domain.repository.UserRepository
import javax.inject.Inject

class AuthUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val cryptoManager: CryptoManager,
    private val userDataStore: UserDataStore
) {
    suspend fun register(
        name: String,
        nickname: String,
        password: String,
        paymentCard: PaymentCard
    ): Result<Unit> {
        return try {

            if (userDataStore.doesUserExist(nickname = nickname)) {
                return Result.failure(Exception("User with this nickname already exists locally"))
            }

            val user = User(
                name = name,
                nickname = nickname,
                passwordHash = cryptoManager.hashPassword(password),
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

                userDataStore.saveUser(registeredUser)
                return Result.success(Unit)
            }

            return Result.failure(result.exceptionOrNull() ?: Exception("Failed to register user"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(
        nickname: String,
        password: String
    ): Result<Unit> {
        return userDataStore.loginUser(nickname = nickname, password = password)
    }
}