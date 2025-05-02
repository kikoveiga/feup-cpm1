package com.feup.client.data.repository

import com.feup.client.data.dto.ErrorResponseDto
import com.feup.client.data.mapper.toRegisterUserRequestDto
import com.feup.client.data.remote.SupermarketApi
import com.feup.client.domain.crypto.CryptoManager
import com.feup.client.domain.model.User
import com.feup.client.domain.repository.UserRepository
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
                val error = Json.decodeFromString<ErrorResponseDto>(errorBody ?: "")
                error.message ?: "Unknown error"
            } catch (parseError: Exception) {
                errorBody ?: "Unknown error"
            }
            Result.failure(Exception(message))

        } catch (e: UnknownHostException) {
            Result.failure(Exception("Failed to connect to server"))
        } catch (e: ConnectException) {
            Result.failure(Exception("Failed to connect to server"))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection to server timed out"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}