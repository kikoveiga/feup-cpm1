package com.feup.terminal.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val uuid: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
)