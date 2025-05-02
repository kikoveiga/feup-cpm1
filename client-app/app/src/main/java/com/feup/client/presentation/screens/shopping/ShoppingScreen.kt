package com.feup.client.presentation.screens.shopping

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val showCheckoutDialog = remember { mutableStateOf(false) }
    val useVouchers = remember { mutableStateOf(false) }
    val useAccumulatedDiscount = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchVouchers()
        viewModel.fetchAccumulatedDiscount()
    }

    val launcher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            scope.launch {
                viewModel.handleQrScan(result.contents)
            }
        }
    }

    val scanOptions = ScanOptions().apply {
        setOrientationLocked(false)
        setPrompt("Scan a product QR code")
        setBeepEnabled(false)
    }

    if (state.value.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            },
            title = { Text("Error") },
            text = { Text(state.value.error!!) }
        )
    }

    if (showCheckoutDialog.value) {
        val qrContent = state.value.qrContent

        if (qrContent != null) {
            val qrBitmap = remember(qrContent) { generateQrCode(qrContent) }

            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearCart()
                            showCheckoutDialog.value = false
                            useVouchers.value = false
                            useAccumulatedDiscount.value = false
                            viewModel.fetchVouchers()
                            viewModel.setUseVouchers(false)
                            viewModel.fetchAccumulatedDiscount()
                            viewModel.setUseAccumulatedDiscount(false)
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showCheckoutDialog.value = false }
                    ) {
                        Text("Cancel")
                    }
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Total: %.2f€".format(state.value.totalPrice))
                        Spacer(Modifier.height(16.dp))
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "Transaction QR"
                        )
                        Spacer(Modifier.height(16.dp))
                        Text("Show this QR code to the terminal app to finish your purchase.")
                    }
                },
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Shopping Cart",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Use Vouchers: ${state.value.vouchers.size}")
                Switch(
                    checked = useVouchers.value,
                    onCheckedChange = {
                        if (state.value.vouchers.isNotEmpty()) {
                            useVouchers.value = it
                            viewModel.setUseVouchers(it)
                        }
                    }
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val discountFormatted = "%.2f€".format(state.value.accumulatedDiscount ?: 0.0)
                Text("Use Accumulated Discount: $discountFormatted")
                Switch(
                    checked = useAccumulatedDiscount.value,
                    onCheckedChange = {
                        useAccumulatedDiscount.value = it
                        viewModel.setUseAccumulatedDiscount(it)
                    },
                    enabled = state.value.accumulatedDiscount > BigDecimal.ZERO
                )
            }


            if (state.value.scannedProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your cart is empty.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    items(
                        state.value.scannedProducts.values.toList(),
                        key = { it.productUuid }) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = product.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "${product.price}€",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        viewModel.updateQuantity(
                                            product.productUuid,
                                            -1
                                        )
                                    }) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                    }
                                    Text(
                                        text = product.quantity.toString(),
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    IconButton(onClick = {
                                        viewModel.updateQuantity(
                                            product.productUuid,
                                            1
                                        )
                                    }) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(onClick = {
                                        viewModel.removeProduct(product.productUuid)
                                    }) {
                                        Icon(Icons.Outlined.Delete, contentDescription = "Remove")
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.value.scannedProducts.isNotEmpty()) {
                    Button(
                        onClick = {
                            showCheckoutDialog.value = true
                            viewModel.generateTransactionQrContent()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Checkout")
                    }
                }

                Button(
                    onClick = { launcher.launch(scanOptions) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Scan Product")
                }
            }
        }
    }
}

fun generateQrCode(text: String, size: Int = 400): Bitmap {
    val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bmp = createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp[x, y] =
                if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }
    return bmp
}
