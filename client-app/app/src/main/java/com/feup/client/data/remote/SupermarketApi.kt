package com.feup.client.data.remote

import com.feup.client.data.dto.RegisterUserRequestDto
import com.feup.client.data.dto.RegisterUserResponseDto
import com.feup.client.data.dto.TransactionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.math.BigDecimal

interface SupermarketApi {

    @POST("/api/register")
    suspend fun registerUser(
        @Body request: RegisterUserRequestDto
    ): RegisterUserResponseDto

    @GET("/api/vouchers")
    suspend fun getVouchers(
        @Query("userUuid") userUuid: String,
        @Query("nonce") nonce: String,
        @Query("signature") signature: String,
    ): List<String>

    @GET("/api/transactions")
    suspend fun getTransactions(
        @Query("userUuid") userUuid: String,
        @Query("nonce") nonce: String,
        @Query("signature") signature: String,
    ): List<TransactionDto>

    @GET("/api/accumulated-discount")
    suspend fun getAccumulatedDiscount(
        @Query("userUuid") userUuid: String,
        @Query("nonce") nonce: String,
        @Query("signature") signature: String
    ): BigDecimal
}