package com.feup.client.data.remote

import com.feup.client.data.model.dto.RegisterUserRequestDto
import com.feup.client.data.model.dto.RegisterUserResponseDto
import com.feup.client.data.model.dto.TransactionDto
import com.feup.client.data.model.dto.UserUuidRequestDto
import com.feup.client.data.model.dto.VoucherDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SupermarketApi {

    @POST("/api/register")
    suspend fun registerUser(
        @Body request: RegisterUserRequestDto
    ): RegisterUserResponseDto

    @GET("/api/vouchers")
    suspend fun getVouchers(
        @Query("uuid") uuid: String,
    ): List<VoucherDto>

    @POST("/api/transactions")
    suspend fun getTransactions(
        @Body requestDto: UserUuidRequestDto
    ): List<TransactionDto>
}