package com.feup.client.data.repository

import com.feup.client.data.local.database.dao.TransactionWithProductsDao
import com.feup.client.data.local.database.entity.TransactionWithProducts
import com.feup.client.data.mapper.toDomain
import com.feup.client.data.mapper.toEntity
import com.feup.client.data.model.dto.UserUuidRequestDto
import com.feup.client.data.remote.SupermarketApi
import com.feup.client.domain.model.Transaction
import com.feup.client.domain.repository.TransactionRepository
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionWithProductsDao: TransactionWithProductsDao,
    private val api: SupermarketApi
) : TransactionRepository {

    override fun getLocalTransactions(userUuid: String): List<Transaction> {
        return transactionWithProductsDao.getAllTransactionsWithProducts(userUuid).map { it.toDomain() }
    }

    override suspend fun fetchAndStoreRemoteTransactions(userUuid: String): Result<Unit> {
        return try {

            val remoteTransactions = api.getTransactions(UserUuidRequestDto(userUuid))
            transactionWithProductsDao.deleteAll(userUuid)
            val transactionsWithProducts = remoteTransactions.map { transactionDto ->
                val transactionEntity = transactionDto.toEntity(userUuid)
                val productEntities = transactionDto.products.map { it.toEntity(transactionDto.transactionUuid) }
                TransactionWithProducts(transactionEntity, productEntities)
            }

            transactionsWithProducts.forEach { (transactionEntity, products) ->
                transactionWithProductsDao.insertTransactionBatch(transactionEntity, products)
            }

            Result.success(Unit)
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Failed to connect to server"))
        } catch (e: ConnectException) {
            Result.failure(Exception("Failed to connect to server"))
        } catch (e: SocketTimeoutException) {
            Result.failure(Exception("Connection to server timed out"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}