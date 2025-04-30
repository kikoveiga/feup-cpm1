package com.feup.client.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.feup.client.data.local.database.dao.TransactionWithProductsDao
import com.feup.client.data.local.database.dao.VoucherDao
import com.feup.client.data.local.database.entity.ProductEntity
import com.feup.client.data.local.database.entity.TransactionEntity
import com.feup.client.data.local.database.entity.VoucherEntity

@Database(entities = [TransactionEntity::class, ProductEntity::class, VoucherEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionWithProductsDao
    abstract fun voucherDao(): VoucherDao
}