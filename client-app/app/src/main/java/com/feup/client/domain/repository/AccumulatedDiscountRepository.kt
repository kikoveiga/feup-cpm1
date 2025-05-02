package com.feup.client.domain.repository

import java.math.BigDecimal

interface AccumulatedDiscountRepository {
    suspend fun fetchAccumulatedDiscount(userUuid: String, nonce: String, signature: String): Result<BigDecimal>
}
