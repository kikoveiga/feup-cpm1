package com.acme.supermarket.server.repository

import com.acme.supermarket.server.domain.User
import com.acme.supermarket.server.domain.Voucher
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VoucherRepository : JpaRepository<Voucher, String> {
    fun findByUuid(uuid: String): Voucher?

    @Modifying
    @Transactional
    @Query("UPDATE Voucher v SET v.used = true WHERE v.uuid = :voucherUuid")
    fun markVoucherAsUsed(voucherUuid: String): Int

    fun findByUserAndUsedFalse(user: User): List<Voucher>

}