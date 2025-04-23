package com.feup.client.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey (autoGenerate = true) val id: Long = 0,
    val date: String,
    val price: Double,
    val discount: Double,
    val voucherUsed: String? = null,
)

@Entity(tableName = "products")
data class ProductEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: Long,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
)

data class TransactionWithProducts(
    @Embedded val transactionEntity: TransactionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "transactionId"
    )
    val products: List<ProductEntity>
)