package com.feup.jtp.checkout_terminal.data.remote

import com.feup.jtp.checkout_terminal.data.model.dto.TransactionToServer
import com.feup.jtp.checkout_terminal.data.model.dto.TransactionFromServer
import retrofit2.http.Body
import retrofit2.http.POST

interface TerminalApi {

    @POST("/api/checkout")
    suspend fun checkoutCart(
        @Body request: TransactionToServer
    ): TransactionFromServer
}
