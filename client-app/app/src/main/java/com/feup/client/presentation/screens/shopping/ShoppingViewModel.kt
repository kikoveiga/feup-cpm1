package com.feup.client.presentation.screens.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.client.data.local.database.dao.VoucherDao
import com.feup.client.domain.local.UserDataStore
import com.feup.client.domain.repository.VoucherRepository
import com.feup.client.domain.usecases.FetchAccumulatedDiscountUseCase
import com.feup.client.domain.usecases.FetchTransactionsUseCase
import com.feup.client.domain.usecases.FetchVouchersUseCase
import com.feup.client.domain.usecases.GenerateTransactionQrUseCase
import com.feup.client.domain.usecases.ScanProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val scanProductUseCase: ScanProductUseCase,
    private val generateTransactionQrUseCase: GenerateTransactionQrUseCase,
    private val fetchVouchersUseCase: FetchVouchersUseCase,
    private val fetchAccumulatedDiscountUseCase: FetchAccumulatedDiscountUseCase,
    private val userDataStore: UserDataStore
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState

    fun handleQrScan(base64Content: String) {
        scanProductUseCase.invoke(base64Content).onSuccess { product ->
            _uiState.update { state ->
                val updated = state.scannedProducts.toMutableMap()
                val existing = updated[product.productUuid]

                val updatedProduct = existing?.copy(quantity = existing.quantity + 1) ?: product.copy(quantity = 1)

                updated[product.productUuid] = updatedProduct
                state.copy(scannedProducts = updated)
            }
            updateTotalPrice()
        }.onFailure {
            _uiState.update { it.copy(error = "Failed to scan product") }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun updateQuantity(uuid: String, delta: Int) {
        _uiState.update { state ->
            val updated = state.scannedProducts.toMutableMap()
            val product = updated[uuid]

            if (product != null) {
                val newQuantity = product.quantity + delta

                if (newQuantity > 0) {
                    updated[uuid] = product.copy(quantity = newQuantity)
                }
            }

            state.copy(scannedProducts = updated)
        }

        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val total = _uiState.value.scannedProducts.values.sumOf { it.price * it.quantity }
        _uiState.update { it.copy(totalPrice = total) }
    }

    fun removeProduct(uuid: String) {
        _uiState.update { state ->
            val updated = state.scannedProducts.toMutableMap()
            updated.remove(uuid)
            state.copy(scannedProducts = updated)
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(scannedProducts = emptyMap()) }
    }

    fun generateTransactionQrContent() {
        viewModelScope.launch {
            val products = _uiState.value.scannedProducts.values.toList()
            val userUuid = userDataStore.getLoggedInUser().uuid ?: throw IllegalStateException("User is not logged in")
            val userNickname = userDataStore.getLoggedInUser().nickname ?: throw IllegalStateException("User is not logged in")

            val useAccumulatedDiscount = _uiState.value.useAccumulatedDiscount
            val voucherId = _uiState.value.appliedVoucher?.voucherUuid

            val transactionDto = generateTransactionQrUseCase.invoke(
                userUuid = userUuid,
                products = products,
                useAccumulatedDiscount = useAccumulatedDiscount,
                voucherId = voucherId,
                userNickname = userNickname
            )

            val qrContent = generateTransactionQrUseCase.toQrContent(transactionDto)

            _uiState.update { it.copy(qrContent = qrContent) }
        }
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

    fun fetchAccumulatedDiscount() {
        viewModelScope.launch {
            val user = userDataStore.getLoggedInUser()
            val userNickname = user.nickname
            val userUuid = user.uuid ?: throw IllegalStateException("User is not logged in")

            fetchAccumulatedDiscountUseCase(userNickname, userUuid).onSuccess { discount ->
                _uiState.update {
                    it.copy(accumulatedDiscount = discount.getOrNull() ?: BigDecimal.ZERO)
                }
            }.onFailure {
                _uiState.update { it.copy(error = "Failed to fetch accumulated discount") }
            }
        }
    }



    fun setUseVouchers(enabled: Boolean) {
        if (enabled && _uiState.value.vouchers.isEmpty()) {
            _uiState.update { it.copy(error = "You do not have any vouchers.") }
        } else {
            val randomVoucher = _uiState.value.vouchers.randomOrNull()
            if (enabled && randomVoucher != null) {
                _uiState.update { it.copy(useVouchers = true, appliedVoucher = randomVoucher) }
            } else {
                _uiState.update { it.copy(useVouchers = false, appliedVoucher = null) }
            }
        }
    }


    fun setUseAccumulatedDiscount(use: Boolean) {
        _uiState.update {
            it.copy(useAccumulatedDiscount = use)
        }
    }


}