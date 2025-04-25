package com.acme.supermarket.server.dto

data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
)

