package com.feup.client.presentation.screens.vouchers

import com.feup.client.domain.model.Voucher

data class VouchersUiState(
    val vouchers: List<Voucher> = emptyList(),
    val error: String? = null
)
