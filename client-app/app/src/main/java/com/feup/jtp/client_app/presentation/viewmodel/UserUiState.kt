package com.feup.jtp.client_app.presentation.viewmodel

import com.feup.jtp.client_app.domain.model.Transaction
import com.feup.jtp.client_app.domain.model.User

data class UserUiState(
    val name: String = "",
    val nickname: String = "",
    val paymentCardNumber: String = "",
    val expirationDate: String = "",
    val user: User? = null,
    val transactions: List<Transaction> = emptyList(),
    val error: String? = null,
    val showValidationErrors: Boolean = false,
)
