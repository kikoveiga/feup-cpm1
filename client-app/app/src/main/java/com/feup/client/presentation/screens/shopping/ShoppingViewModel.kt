package com.feup.client.presentation.screens.shopping

import androidx.lifecycle.ViewModel
import com.feup.client.domain.usecases.GenerateTransactionQrUseCase
import com.feup.client.domain.usecases.ScanProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val scanProductUseCase: ScanProductUseCase,
    private val generateTransactionQrUseCase: GenerateTransactionQrUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState

    fun handleQrScan(base64Content: String) {
        scanProductUseCase.invoke(base64Content).onSuccess { product ->
            _uiState.update { state ->
                val updated = state.scannedProducts.toMutableMap()
                val existing = updated[product.uuid]

                val updatedProduct = existing?.copy(quantity = existing.quantity + 1) ?: product.copy(quantity = 1)

                updated[product.uuid] = updatedProduct
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
                } else {
                    updated.remove(uuid)
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

    fun getTransactionQrContent(): String {
        val products = _uiState.value.scannedProducts.values.toList()
        val transaction = generateTransactionQrUseCase.invoke(products = products)
        return generateTransactionQrUseCase.toQrContent(transaction)
    }
}