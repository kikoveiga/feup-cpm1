package com.feup.client.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.feup.client.data.model.dto.ProductDto
import com.feup.client.data.model.dto.TransactionDto

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val date: String,
    val price: Double,
    val discount: Double,
    val voucherUsed: String? = null,
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val transactionId: String,
    val name: String,
    val price: Double,
    val quantity: Int = 1
)

data class TransactionWithProducts(
    @Embedded val transactionEntity: TransactionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "transactionId"
    )
    val products: List<ProductEntity>
)

// Funções de mapeamento para conversão entre DTO e Entidades

// Função de mapeamento de TransactionDto para TransactionEntity
fun TransactionDto.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        date = this.date,
        price = this.price,
        discount = this.discount,
        voucherUsed = this.voucherUsed?.id
    )
}

fun ProductDto.toEntity(transactionId: String, quantity: Int): ProductEntity {
    return ProductEntity(
        id = this.id,
        transactionId = transactionId,
        name = this.name,
        price = this.price,
        quantity = quantity
    )
}

fun TransactionDto.toTransactionWithProducts(): TransactionWithProducts {
    val transactionEntity = this.toEntity()
    val productEntities = this.products.map { it.toEntity(transactionEntity.id, it.quantity) }

    return TransactionWithProducts(
        transactionEntity = transactionEntity,
        products = productEntities
    )
}
