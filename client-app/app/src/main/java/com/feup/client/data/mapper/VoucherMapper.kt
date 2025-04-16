package com.feup.client.data.mapper

import com.feup.client.data.model.dto.VoucherDto
import com.feup.client.domain.model.Voucher

fun VoucherDto.toDomain(): Voucher =
    Voucher(
        id = id,
        isUsed = isUsed
    )