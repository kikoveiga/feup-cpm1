package com.feup.client.presentation.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.usecases.FetchTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val fetchTransactionsUseCase: FetchTransactionsUseCase,
    private val userDataStore: UserDataStore
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val userUuid = userDataStore.getLoggedInUser().uuid ?: return@launch
            val localTransactions = fetchTransactionsUseCase.local(userUuid)
            _uiState.update {
                it.copy(transactions = localTransactions)
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
                val result = fetchTransactionsUseCase.remote(userUuid)

                result.fold(

                    onSuccess = { updatedTransactions ->
                        _uiState.update {
                            it.copy(transactions = updatedTransactions, error = null)
                        }
                    },

                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(error = exception.message)
                        }
                    }
                )
            }
        }
    }
}
