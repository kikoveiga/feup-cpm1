package com.feup.client.data.mapper

import com.feup.client.data.local.database.entity.ProductEntity
import com.feup.client.data.local.database.entity.TransactionEntity
import com.feup.client.data.local.database.entity.TransactionWithProducts
import com.feup.client.data.model.dto.ProductDto
import com.feup.client.data.model.dto.TransactionDto
import com.feup.client.domain.model.Product
import com.feup.client.domain.model.Transaction

fun ProductDto.toDomain(): Product =
    Product(
        uuid = id,
        name = name,
        price = price,
        quantity = quantity
    )

fun ProductDto.toEntity(transactionId: String): ProductEntity =
    ProductEntity(
        id = id.toLong(),
        transactionId = transactionId.toLong(),
        name = name,
        price = price,
        quantity = quantity
    )

fun ProductEntity.toDomain(): Product =
    Product(
        uuid = id.toString(),
        name = name,
        price = price,
        quantity = quantity
    )

fun TransactionDto.toEntity(): TransactionEntity =
    TransactionEntity(
        id = id.toLong(),
        date = date,
        price = price,
        discount = discount,
    )

fun TransactionDto.toDomain(): Transaction =
    Transaction(
        id = id,
        date = date,
        products = products.map { it.toDomain() },
        price = price,
        discount = discount,
        voucherUsed = voucherUsed?.toDomain()
    )

fun TransactionWithProducts.toDomain(): Transaction =
    Transaction(
        id = transactionEntity.id.toString(),
        date = transactionEntity.date,
        products = products.map { it.toDomain() },
        price = transactionEntity.price,
        discount = transactionEntity.discount,
    )