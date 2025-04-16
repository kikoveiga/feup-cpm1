package com.acme.supermarket.server.repository

import com.acme.supermarket.server.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal

interface UserRepository : JpaRepository<User, Long> {
    fun findByUserUuid(userUuid: String): User?

    @Query("SELECT u.accumulatedDiscount FROM User u WHERE u.userUuid = :userUuid")
    fun getAccumulatedDiscount(@Param("userUuid") userUuid: String): BigDecimal?

    @Modifying
    @Query("UPDATE User u SET u.totalSpent = :total WHERE u.userUuid = :userId")
    fun updateTotalSpent(@Param("userId") userId: String, @Param("total") total: BigDecimal)

    @Modifying
    @Query("UPDATE User u SET u.accumulatedDiscount = :accumulatedDiscount WHERE u.userUuid = :userId")
    fun updateAccumulatedDiscount(@Param("userId") userId: String, @Param("accumulatedDiscount") accumulatedDiscount: BigDecimal)
}