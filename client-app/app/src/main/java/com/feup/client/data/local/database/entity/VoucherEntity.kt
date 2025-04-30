package com.feup.client.data.local.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vouchers",
    indices = [Index("userUuid")]
)
data class VoucherEntity(
    @PrimaryKey val voucherUuid: String,
    val userUuid: String,
    val isUsed: Boolean = false
)