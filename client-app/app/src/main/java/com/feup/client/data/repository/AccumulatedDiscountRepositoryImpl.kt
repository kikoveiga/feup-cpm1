package com.feup.client.data.repository

import com.feup.client.data.remote.SupermarketApi
import com.feup.client.domain.repository.AccumulatedDiscountRepository
import java.math.BigDecimal
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AccumulatedDiscountRepositoryImpl @Inject constructor(
    private val api: SupermarketApi
) : AccumulatedDiscountRepository {

    override suspend fun fetchAccumulatedDiscount(
        userUuid: String,
        nonce: String,
        signature: String
    ): Result<BigDecimal> {
        return try {
            val accumulatedDiscount = api.getAccumulatedDiscount(userUuid, nonce, signature)
            Result.success(accumulatedDiscount)
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
