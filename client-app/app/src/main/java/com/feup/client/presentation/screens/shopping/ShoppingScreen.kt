package com.feup.client.presentation.screens.shopping

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch

@Composable
fun ShoppingScreen(navController: NavController, viewModel: ShoppingViewModel = hiltViewModel()) {
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Button(onClick = { launcher.launch(scanOptions) }) {
            Text("Scan product")
        }
        Spacer(modifier = Modifier.height(16.dp))

        state.value.scannedProducts.forEach { product ->
            Text("Scanned: ${product.name} - ${product.price}€")
        }
    }

}