package com.feup.terminal.domain.model

data class Product(
    val uuid: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
)