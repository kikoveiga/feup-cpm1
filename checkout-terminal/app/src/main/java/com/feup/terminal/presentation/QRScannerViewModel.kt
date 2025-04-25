package com.feup.terminal.presentation

import androidx.lifecycle.ViewModel
import com.feup.terminal.domain.usecases.ScanTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(
    private val scanTransactionUseCase: ScanTransactionUseCase
) : ViewModel() {

    fun handleQrScan(base64Content: String, onResult: (Boolean) -> Unit) {
        scanTransactionUseCase.invoke(base64Content).onSuccess {

        }
    }
}
