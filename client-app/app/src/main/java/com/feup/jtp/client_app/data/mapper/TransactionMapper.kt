package com.feup.jtp.client_app.data.mapper

import com.feup.jtp.client_app.data.model.dto.TransactionDto
import com.feup.jtp.client_app.domain.model.Transaction

fun TransactionDto.toDomain(): Transaction =
    Transaction(
        id = id,
        date = date,
        products = products.map { it.toDomain() },
        price = price,
        discount = discount,
        voucherUsed = voucherUsed?.toDomain()
    )