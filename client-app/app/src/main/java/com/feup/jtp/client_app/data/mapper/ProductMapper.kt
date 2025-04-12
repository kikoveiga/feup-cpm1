package com.feup.jtp.client_app.data.mapper

import com.feup.jtp.client_app.data.model.dto.ProductDto
import com.feup.jtp.client_app.domain.model.Product

fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        name = name,
        price = price
    )