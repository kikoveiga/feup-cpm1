package com.feup.jtp.client_app.presentation.screens.transactions

import com.feup.jtp.client_app.domain.model.Transaction

data class TransactionsUiState (
    val transactions: List<Transaction> = emptyList()
)