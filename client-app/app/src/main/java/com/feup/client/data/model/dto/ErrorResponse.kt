package com.feup.client.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val timestamp: String? = null,
    val status: String? = null,
    val error: String? = null,
    val message: String? = null,
    val path: String? = null,
)