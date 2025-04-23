package com.feup.client.presentation.screens.shopping

import com.feup.client.domain.model.Product

data class ShoppingUiState(
    val scannedProducts: Map<String, Product> = emptyMap(),
    val error: String? = null,
)