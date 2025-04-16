package com.feup.client.data.mapper

import com.feup.client.data.model.dto.TransactionDto
import com.feup.client.domain.model.Transaction

fun TransactionDto.toDomain(): Transaction =
    Transaction(
        id = id,
        date = date,
        products = products.map { it.toDomain() },
        price = price,
        discount = discount,
        voucherUsed = voucherUsed?.toDomain()
    )