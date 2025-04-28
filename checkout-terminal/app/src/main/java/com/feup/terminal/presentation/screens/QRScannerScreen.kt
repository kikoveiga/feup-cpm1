package com.feup.terminal.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.feup.terminal.presentation.QRScannerViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch


@Composable
fun QRScannerScreen(
    onResult: (Boolean) -> Unit,
    viewModel: QRScannerViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            scope.launch {
                viewModel.handleQrScan(result.contents) { success ->
                    onResult(success)
                }
            }
        } else {
            onResult(false)
        }
    }

    val scanOptions = ScanOptions().apply {
        setOrientationLocked(false)
        setPrompt("Scan a QR code")
        setBeepEnabled(false)
    }

    LaunchedEffect(Unit) {
        launcher.launch(scanOptions)
    }
}
