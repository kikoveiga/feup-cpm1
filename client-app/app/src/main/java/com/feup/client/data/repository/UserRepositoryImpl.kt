package com.feup.client.data.repository

import com.feup.client.data.mapper.toDomain
import com.feup.client.data.mapper.toRegisterUserRequestDto
import com.feup.client.data.model.dto.ErrorResponse
import com.feup.client.data.remote.SupermarketApi
import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.Transaction
import com.feup.client.domain.model.User
import com.feup.client.domain.model.Voucher
import com.feup.client.domain.repository.UserRepository
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: SupermarketApi,
    private val cryptoManager: CryptoManager
) : UserRepository {

    override suspend fun registerUser(user: User): Result<User> {
        val requestDto = user.toRegisterUserRequestDto(cryptoManager)

        return try {
            val responseDto = api.registerUser(requestDto)
            val registeredUser = user.copy(uuid = responseDto.uuid, supermarketRsaPublicKey = cryptoManager.parsePemPublicKey(responseDto.supermarketRsaPublicKey, "RSA"))
            Result.success(registeredUser)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val message = try {
                val error = Json.decodeFromString<ErrorResponse>(errorBody ?: "")
                error.message ?: "Unknown error"
            } catch (parseError: Exception) {
                errorBody ?: "Unknown error"
            }
            Result.failure(Exception(message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVouchers(uuid: String): List<Voucher> =
        api.getVouchers(uuid).map { it.toDomain() }

    override suspend fun getTransactions(uuid: String): List<Transaction> =
        api.getTransactions(uuid).map { it.toDomain() }
}