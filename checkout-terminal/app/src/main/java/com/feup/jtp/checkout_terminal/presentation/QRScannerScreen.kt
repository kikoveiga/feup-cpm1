package com.feup.jtp.checkout_terminal.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.CompoundBarcodeView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Text


@Composable
fun QRScannerScreen(
    onResult: (Boolean) -> Unit,
    viewModel: QRScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cameraPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> cameraPermissionGranted.value = granted }
    )

    LaunchedEffect(Unit) {
        if (!cameraPermissionGranted.value) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (cameraPermissionGranted.value) {
        var hasScanned by remember { mutableStateOf(false) }

        AndroidView(
            factory = { ctx ->
                val barcodeView = CompoundBarcodeView(ctx).apply {
                    barcodeView.decoderFactory = DefaultDecoderFactory(listOf(BarcodeFormat.QR_CODE))
                    decodeContinuous { result ->
                        if (!hasScanned) {
                            hasScanned = true
                            val qrCode = result.text
                            viewModel.sendQRCode(qrCode) { success ->
                                onResult(success)
                            }
                        }
                    }

                    post {
                        resume()
                    }
                }
                barcodeView
            },
            modifier = Modifier
        )
    } else {
        Text("Waiting for camera permission...")
    }
}
