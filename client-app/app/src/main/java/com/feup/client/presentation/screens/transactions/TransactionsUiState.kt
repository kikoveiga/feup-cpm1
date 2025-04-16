package com.feup.client.presentation.screens.transactions

import com.feup.client.domain.model.Transaction

data class TransactionsUiState (
    val transactions: List<Transaction> = emptyList()
)