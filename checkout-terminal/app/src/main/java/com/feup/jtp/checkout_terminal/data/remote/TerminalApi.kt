package com.feup.jtp.checkout_terminal.data.remote

import com.feup.jtp.checkout_terminal.data.model.dto.TransactionToServer
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TerminalApi {

    @POST("/api/checkout")
    suspend fun checkoutCart(
        @Body request: TransactionToServer
    ): TransactionToServer

    @GET("/api/cart")
    suspend fun getCartForUser(
        @Query("uuid") uuid: String,
    ): CartDto
}
