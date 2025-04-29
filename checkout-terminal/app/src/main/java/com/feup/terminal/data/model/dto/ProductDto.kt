package com.feup.terminal.data.model.dto

data class ProductDto(
    val productUuid: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
)

