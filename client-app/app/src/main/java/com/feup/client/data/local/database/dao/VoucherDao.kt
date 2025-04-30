package com.feup.client.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.feup.client.data.local.database.entity.VoucherEntity

@Dao
interface VoucherDao {

    @Query("SELECT * FROM vouchers WHERE isUsed = 0 AND userUuid = :userUuid")
    fun getUnusedVouchers(userUuid: String): List<VoucherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVouchers(vouchers: List<VoucherEntity>)

    @Query("UPDATE vouchers SET isUsed = 1 WHERE voucherUuid = :voucherUuid")
    suspend fun markVoucherAsUsed(voucherUuid: String)

    @Query("DELETE FROM vouchers WHERE userUuid = :userUuid")
    suspend fun clearVouchersForUser(userUuid: String)
}