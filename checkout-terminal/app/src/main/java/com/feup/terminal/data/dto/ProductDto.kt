package com.feup.terminal.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val productUuid: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
)