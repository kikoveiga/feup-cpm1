package com.feup.jtp.client_app.data.mapper

import com.feup.jtp.client_app.data.model.dto.VoucherDto
import com.feup.jtp.client_app.domain.model.Voucher

fun VoucherDto.toDomain(): Voucher =
    Voucher(
        id = id,
        isUsed = isUsed
    )