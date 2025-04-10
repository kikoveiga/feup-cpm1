package com.feup.jtp.client_app.domain.repository

import com.feup.jtp.client_app.domain.model.Transaction
import com.feup.jtp.client_app.domain.model.User
import com.feup.jtp.client_app.domain.model.Voucher

interface UserRepository {

    suspend fun registerUser(user: User): User
    suspend fun getVouchers(uuid: String): List<Voucher>
    suspend fun getTransactions(uuid: String): List<Transaction>
}