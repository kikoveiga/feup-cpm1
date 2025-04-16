package com.feup.client.data.mapper

import com.feup.client.data.model.dto.ProductDto
import com.feup.client.domain.model.Product

fun ProductDto.toDomain(): Product =
    Product(
        id = id,
        name = name,
        price = price
    )