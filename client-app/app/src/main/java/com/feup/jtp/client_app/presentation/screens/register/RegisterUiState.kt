package com.feup.jtp.client_app.presentation.screens.register

import com.feup.jtp.client_app.domain.model.PaymentCardType

data class RegisterUiState(
    val name: String = "",
    val nickname: String = "",
    val paymentCardType: PaymentCardType = PaymentCardType.DEBIT,
    val paymentCardNumber: String = "",
    val paymentCardExpirationDate: String = "",
    val error: String? = null,
    val showValidationErrors: Boolean = false,
)
