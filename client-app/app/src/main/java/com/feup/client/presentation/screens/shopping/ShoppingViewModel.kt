package com.feup.client.presentation.screens.shopping

import androidx.lifecycle.ViewModel
import com.feup.client.domain.usecases.ScanProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val scanProductUseCase: ScanProductUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState

    suspend fun handleQrScan(base64Content: String) {
        scanProductUseCase.invoke(base64Content).let { product ->
            _uiState.update { it.copy(scannedProducts = it.scannedProducts + product) }
        }
    }
}