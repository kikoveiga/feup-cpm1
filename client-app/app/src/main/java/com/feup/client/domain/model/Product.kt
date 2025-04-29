package com.feup.client.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val productUuid: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
)
