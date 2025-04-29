package com.feup.terminal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.terminal.data.model.dto.ProductDto
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

    fun handleQrScan(base64Content: String, onResult: (Boolean) -> Unit) {
        println("Scanned QR content: $base64Content") // <-- Print the raw QR scan

        scanTransactionUseCase.invoke(base64Content).onSuccess { transactionData ->
            println("Parsed transaction data: $transactionData") // <-- Print the parsed transaction

            val transactionToServerDto = transactionToDto(transactionData)

            if (!validateTransaction(transactionToServerDto)) {
                onResult(false)
                return@onSuccess
            }

            sendTransactionToServer(transactionToServerDto, onResult)

        }.onFailure {
            it.printStackTrace()
            onResult(false)
        }
    }


    private fun sendTransactionToServer(dto: TransactionToServerDto, onResult: (Boolean) -> Unit) {
        // Need to launch coroutine outside
        viewModelScope.launch {
            try {
                val response = terminalApi.sendTransactionToServer(dto)

                onResult(response.isSuccess)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    private fun transactionToDto(transaction: Transaction): TransactionToServerDto {
        return TransactionToServerDto(
            userUuid = transaction.userUuid,
            date = transaction.date,
            products = transaction.products.map { product ->
                ProductDto(
                    productUuid = product.productUuid,
                    price = product.price,
                    name = product.name,
                    quantity = product.quantity
                )
            },
            voucherId = transaction.voucherUsed?.id,
            useAccumulatedDiscount = transaction.discount > 0.0,
            signature = "AAAAAAAA" // <-- Here you need to get the signature! (is it stored somewhere in Transaction?)
        )
    }

    private fun validateTransaction(transactionData: TransactionToServerDto): Boolean {
        if (transactionData.userUuid.isBlank()) return false
        if (transactionData.products.isEmpty() || transactionData.products.size > 10) return false
        if (transactionData.products.any { it.productUuid.isBlank() || it.price < 0.0 }) return false

        return true
    }
}
