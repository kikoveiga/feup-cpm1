package com.feup.jtp.checkout_terminal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.jtp.checkout_terminal.data.model.dto.TransactionToServer
import com.feup.jtp.checkout_terminal.data.remote.TerminalApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(
    private val terminalApi: TerminalApi
) : ViewModel() {

    fun sendQRCode(qrCode: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = terminalApi.checkoutCart(TransactionToServer(qrCode))
                onResult(response.isSuccess)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }
}
