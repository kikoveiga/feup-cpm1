package com.feup.terminal.data.remote

import com.feup.terminal.data.dto.TransactionFromServerDto
import com.feup.terminal.data.dto.TransactionToServerDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TerminalApi {

    @POST("/api/checkout")
    suspend fun sendTransactionToServer(
        @Body request: TransactionToServerDto
    ): TransactionFromServerDto
}
