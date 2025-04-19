package com.feup.client.presentation.screens.register

import com.feup.client.domain.model.PaymentCardType

data class RegisterUiState(
    val name: String = "",
    val nameError: String? = null,

    val nickname: String = "",
    val nicknameError: String? = null,

    val password: String = "",
    val passwordError: String? = null,

    val paymentCardType: PaymentCardType = PaymentCardType.DEBIT,

    val paymentCardNumber: String = "",
    val paymentCardNumberError: String? = null,

    val paymentCardExpirationDate: String = "",
    val error: String? = null,
    val showValidationErrors: Boolean = false,
)
