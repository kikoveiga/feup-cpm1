package com.feup.client.data.dto

data class ProductDto(
    val productUuid: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
)
