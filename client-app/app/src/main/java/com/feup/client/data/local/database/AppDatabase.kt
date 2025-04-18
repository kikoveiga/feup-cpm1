package com.feup.client.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.feup.client.data.local.database.dao.TransactionWithProductsDao
import com.feup.client.data.local.database.entity.ProductEntity
import com.feup.client.data.local.database.entity.TransactionEntity

@Database(entities = [TransactionEntity::class, ProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionWithProductsDao
}