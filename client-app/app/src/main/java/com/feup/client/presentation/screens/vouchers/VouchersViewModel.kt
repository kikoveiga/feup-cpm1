package com.feup.client.presentation.screens.vouchers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.model.Voucher
import com.feup.client.domain.usecases.FetchVouchersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VouchersViewModel @Inject constructor(
    private val fetchVouchersUseCase: FetchVouchersUseCase,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(VouchersUiState())
    val uiState: StateFlow<VouchersUiState> = _uiState

    init {
        fetchVouchers()
    }

    fun fetchVouchers() {
        viewModelScope.launch {
            val userNickname = userDataStore.getLoggedInUser().nickname
            val userUuid = userDataStore.getLoggedInUser().uuid ?: throw IllegalStateException("User is not logged in")
            fetchVouchersUseCase(userNickname, userUuid).onSuccess { vouchers ->
                _uiState.update { it.copy(vouchers = vouchers) }
            }.onFailure {
                _uiState.update { it.copy(error = "Failed to fetch vouchers") }
            }
        }
    }


    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
