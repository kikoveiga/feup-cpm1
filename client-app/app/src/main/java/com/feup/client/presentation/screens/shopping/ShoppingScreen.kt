package com.feup.client.presentation.screens.shopping

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch

@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { launcher.launch(scanOptions) }) {
            Text("Scan Product")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Shopping Cart",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.value.scannedProducts.isEmpty()) {
            Text("Your cart is empty.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.value.scannedProducts.values.toList(), key = { it.uuid }) { product ->
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
                                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = "${product.price}€", style = MaterialTheme.typography.bodyMedium)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.updateQuantity(product.uuid,-1) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                }
                                Text(
                                    text = product.quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(onClick = { viewModel.updateQuantity(product.uuid, 1) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { viewModel.removeProduct(product.uuid) }) {
                                    Icon(Icons.Outlined.Delete, contentDescription = "Remove")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
