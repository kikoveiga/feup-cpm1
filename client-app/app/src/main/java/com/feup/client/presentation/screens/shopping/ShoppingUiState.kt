package com.feup.client.presentation.screens.shopping

import com.feup.client.domain.model.Product

data class ShoppingUiState(
    val scannedProducts: Map<String, Product> = emptyMap(),
    val totalPrice: Double = 0.0,
    val qrContent: String? = null,
    val error: String? = null,
)