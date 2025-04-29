package com.feup.terminal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.terminal.data.model.dto.ProductDto
import com.feup.terminal.data.model.dto.TransactionFromServerDto
import com.feup.terminal.data.model.dto.TransactionToServerDto
import com.feup.terminal.data.remote.TerminalApi
import com.feup.terminal.domain.model.Transaction
import com.feup.terminal.domain.usecases.ScanTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(
    private val scanTransactionUseCase: ScanTransactionUseCase,
    private val terminalApi: TerminalApi
) : ViewModel() {

    fun handleQrScan(base64Content: String, onResult: (TransactionFromServerDto?) -> Unit) {
        println("Scanned QR content: $base64Content") // <-- Print the raw QR scan

        scanTransactionUseCase.invoke(base64Content).onSuccess { transactionData ->
            println("Parsed transaction data: $transactionData") // <-- Print the parsed transaction

            val transactionToServerDto = transactionToDto(transactionData)

            if (!validateTransaction(transactionToServerDto)) {
                onResult(null)
                return@onSuccess
            }

            sendTransactionToServer(transactionToServerDto, onResult)

        }.onFailure {
            it.printStackTrace()
            onResult(null)
        }
    }

    private fun sendTransactionToServer(
        dto: TransactionToServerDto,
        onResult: (TransactionFromServerDto?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = terminalApi.sendTransactionToServer(dto)
                onResult(response)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }

    private fun transactionToDto(transaction: Transaction): TransactionToServerDto {
        return TransactionToServerDto(
            userUuid = transaction.userUuid,
            products = transaction.products.map { product ->
                ProductDto(
                    id = product.uuid,
                    price = product.price,
                    name = product.name
                )
            },
            voucherId = transaction.voucherUsed?.id,
            useAccumulatedDiscount = transaction.discount > 0.0,
            signature = "AAAAAAAA" // TODO: Replace with real signature!
        )
    }

    private fun validateTransaction(transactionData: TransactionToServerDto): Boolean {
        if (transactionData.userUuid.isBlank()) return false
        if (transactionData.products.isEmpty() || transactionData.products.size > 10) return false
        if (transactionData.products.any { it.id.isBlank() || it.price < 0.0 }) return false
        return true
    }
}
