package com.feup.client.data.mapper

import com.feup.client.data.local.database.entity.ProductEntity
import com.feup.client.data.local.database.entity.TransactionEntity
import com.feup.client.data.local.database.entity.TransactionWithProducts
import com.feup.client.data.model.dto.ProductDto
import com.feup.client.data.model.dto.TransactionDto
import com.feup.client.domain.model.Product
import com.feup.client.domain.model.Transaction

fun ProductDto.toEntity(transactionId: String): ProductEntity =
    ProductEntity(
        productUuid = productUuid,
        transactionId = transactionId,
        name = name,
        price = price,
        quantity = quantity
    )

fun ProductEntity.toDomain(): Product =
    Product(
        productUuid = productUuid,
        name = name,
        price = price,
        quantity = quantity
    )

fun TransactionDto.toEntity(userUuid: String): TransactionEntity {
    return TransactionEntity(
        id = transactionUuid,
        userUuid = userUuid,
        date = date,
        price = price,
        discount = discount,
        voucherUsed = voucherUsed?.id
    )
}

fun TransactionWithProducts.toDomain(): Transaction =
    Transaction(
        userUuid = transactionEntity.id,
        date = transactionEntity.date,
        products = products.map { it.toDomain() },
        price = transactionEntity.price,
        discount = transactionEntity.discount,
    )