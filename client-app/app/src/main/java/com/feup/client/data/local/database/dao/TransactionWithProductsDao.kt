package com.feup.client.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.feup.client.data.local.database.entity.ProductEntity
import com.feup.client.data.local.database.entity.TransactionEntity
import com.feup.client.data.local.database.entity.TransactionWithProducts

@Dao
interface TransactionWithProductsDao {

    @Transaction
    @Query("SELECT * FROM transactions WHERE userUuid = :userUuid")
    fun getAllTransactionsWithProducts(userUuid: String): List<TransactionWithProducts>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Transaction
    suspend fun insertTransactionBatch(
        transaction: TransactionEntity,
        products: List<ProductEntity>
    ) {
        insertTransactions(listOf(transaction))
        insertProducts(products)
    }

    @Query("DELETE FROM transactions WHERE userUuid = :userUuid")
    suspend fun deleteAll(userUuid: String)
}