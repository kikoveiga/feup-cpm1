package com.feup.terminal.data.remote

import com.feup.terminal.data.model.dto.TransactionFromServerDto
import com.feup.terminal.data.model.dto.TransactionToServerDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TerminalApi {

    @POST("/api/checkout")
    suspend fun checkoutCart(
        @Body request: TransactionToServerDto
    ): TransactionFromServerDto
}
