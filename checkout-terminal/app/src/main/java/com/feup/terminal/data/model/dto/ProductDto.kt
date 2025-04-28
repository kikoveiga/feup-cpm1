package com.feup.terminal.data.model.dto

data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
)

