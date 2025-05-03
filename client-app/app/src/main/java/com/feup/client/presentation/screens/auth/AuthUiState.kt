package com.feup.client.presentation.screens.auth

import com.feup.client.domain.model.PaymentCardType

data class AuthUiState(
    val name: String = "",
    val password: String = "",
    val nickname: String = "",

    val paymentCardType: PaymentCardType = PaymentCardType.DEBIT,
    val paymentCardNumber: String = "",
    val paymentCardExpirationDate: String = "",

    val error: String? = null,
)
