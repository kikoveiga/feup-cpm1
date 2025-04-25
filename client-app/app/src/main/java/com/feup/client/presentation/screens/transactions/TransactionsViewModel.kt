package com.feup.client.presentation.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val userDataStore: UserDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState

    init {
        // Carregar transações locais inicialmente
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(transactions = transactionRepository.getLocalTransactions())
            }
        }
    }

    fun updateTransactions() {
        viewModelScope.launch(Dispatchers.IO) { // Garantir que a corrotina seja executada em segundo plano
            userDataStore.getLoggedInUser().uuid?.let {
                transactionRepository.fetchAndStoreRemoteTransactions(it)
                // Após obter as transações, atualize o estado
                val updatedTransactions = transactionRepository.getLocalTransactions()
                _uiState.update { currentState ->
                    currentState.copy(transactions = updatedTransactions)
                }
            }
        }
    }
}
