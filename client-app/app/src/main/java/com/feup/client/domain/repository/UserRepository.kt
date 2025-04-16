package com.feup.client.domain.repository

import com.feup.client.domain.model.Transaction
import com.feup.client.domain.model.User
import com.feup.client.domain.model.Voucher

interface UserRepository {

    suspend fun registerUser(user: User): Result<User>
    suspend fun getVouchers(uuid: String): List<Voucher>
    suspend fun getTransactions(uuid: String): List<Transaction>
}