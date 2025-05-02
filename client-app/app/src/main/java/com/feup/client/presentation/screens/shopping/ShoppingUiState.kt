package com.feup.client.presentation.screens.shopping

import com.feup.client.domain.model.Product
import com.feup.client.domain.model.Voucher

data class ShoppingUiState(
    val scannedProducts: Map<String, Product> = emptyMap(),
    val totalPrice: Double = 0.0,
    val qrContent: String? = null,
    val vouchers: List<Voucher> = emptyList(),
    val error: String? = null,
    val useVouchers: Boolean = false,
    val useAccumulatedDiscount: Boolean = false
)