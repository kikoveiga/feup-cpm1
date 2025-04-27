package com.feup.client.presentation.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val userDataStore: UserDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(transactions = transactionRepository.getLocalTransactions())
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun updateTransactions() {
        viewModelScope.launch(Dispatchers.IO) {
            val userUuid = userDataStore.getLoggedInUser().uuid
            if (userUuid != null) {
                val result = transactionRepository.fetchAndStoreRemoteTransactions(userUuid)

                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        val updatedTransactions = transactionRepository.getLocalTransactions()
                        _uiState.update {
                            it.copy(
                                transactions = updatedTransactions,
                                error = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                error = result.exceptionOrNull()?.message
                            )
                        }
                    }
                }
            }
        }
    }
}
