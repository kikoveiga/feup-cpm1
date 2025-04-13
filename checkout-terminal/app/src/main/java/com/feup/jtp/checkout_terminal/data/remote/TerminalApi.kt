package com.feup.jtp.checkout_terminal.data.remote

import com.feup.jtp.checkout_terminal.data.model.dto.CartDto
import com.feup.jtp.checkout_terminal.data.model.dto.TransactionRequestDto
import com.feup.jtp.checkout_terminal.data.model.dto.TransactionResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TerminalApi {

    @POST("/api/checkout")
    suspend fun checkoutCart(
        @Body request: TransactionRequestDto
    ): TransactionResponseDto

    @GET("/api/cart")
    suspend fun getCartForUser(
        @Query("uuid") uuid: String,
    ): CartDto
}
