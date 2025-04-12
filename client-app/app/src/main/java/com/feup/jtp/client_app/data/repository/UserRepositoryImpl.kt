package com.feup.jtp.client_app.data.repository

import com.feup.jtp.client_app.data.mapper.toDomain
import com.feup.jtp.client_app.data.mapper.toRegisterUserRequestDto
import com.feup.jtp.client_app.data.remote.SupermarketApi
import com.feup.jtp.client_app.domain.model.Transaction
import com.feup.jtp.client_app.domain.model.User
import com.feup.jtp.client_app.domain.model.Voucher
import com.feup.jtp.client_app.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: SupermarketApi
) : UserRepository {

    override suspend fun registerUser(user: User): User {
        val requestDto = user.toRegisterUserRequestDto()
        val responseDto = api.registerUser(requestDto)
        return user.copy(uuid = responseDto.uuid)
    }

    override suspend fun getVouchers(uuid: String): List<Voucher> =
        api.getVouchers(uuid).map { it.toDomain() }

    override suspend fun getTransactions(uuid: String): List<Transaction> =
        api.getTransactions(uuid).map { it.toDomain() }
}