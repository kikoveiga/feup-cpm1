package com.feup.client.data.repository

import com.feup.client.data.local.database.dao.TransactionWithProductsDao
import com.feup.client.data.local.database.entity.TransactionWithProducts
import com.feup.client.data.mapper.toDomain
import com.feup.client.data.mapper.toEntity
import com.feup.client.data.model.dto.UuidRequestDto
import com.feup.client.data.remote.SupermarketApi
import com.feup.client.domain.model.Transaction
import com.feup.client.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionWithProductsDao: TransactionWithProductsDao,
    private val api: SupermarketApi
) : TransactionRepository {

    override fun getLocalTransactions(): List<Transaction> {
        return transactionWithProductsDao.getAllTransactionsWithProducts().map { it.toDomain() }
    }

    override suspend fun fetchAndStoreRemoteTransactions(userUuid: String) {
        val remoteTransactions = api.getTransactions(UuidRequestDto(userUuid))
        transactionWithProductsDao.deleteAll()
        val transactionsWithProducts = remoteTransactions.map { transaction ->
            val entity = transaction.toEntity()
            val products = transaction.products.map { it.toEntity(transaction.id) }
            TransactionWithProducts(entity, products)
        }

        transactionsWithProducts.forEach { (transactionEntity, products) ->
            transactionWithProductsDao.insertTransactionBatch(transactionEntity, products)
        }
    }
}