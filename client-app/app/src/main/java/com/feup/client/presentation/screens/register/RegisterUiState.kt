package com.feup.client.presentation.screens.register

import com.feup.client.domain.model.PaymentCardType

data class RegisterUiState(
    val name: String = "",
    val nickname: String = "",
    val paymentCardType: PaymentCardType = PaymentCardType.DEBIT,
    val paymentCardNumber: String = "",
    val paymentCardExpirationDate: String = "",
    val error: String? = null,
    val showValidationErrors: Boolean = false,
)
