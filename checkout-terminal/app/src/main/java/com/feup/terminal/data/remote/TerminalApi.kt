package com.feup.terminal.data.remote

import com.feup.terminal.data.model.dto.TransactionFromServerDto
import com.feup.terminal.data.model.dto.TransactionToServerDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TerminalApi {

    @POST("/api/sendTransactionToServer")
    suspend fun sendTransactionToServer(
        @Body request: TransactionToServerDto
    ): TransactionFromServerDto
}
