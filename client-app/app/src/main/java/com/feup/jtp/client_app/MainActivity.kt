package com.feup.jtp.client_app

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.feup.jtp.client_app.presentation.theme.ClientappTheme
import dagger.hilt.android.AndroidEntryPoint
import com.feup.jtp.client_app.domain.model.Product
import com.feup.jtp.client_app.domain.interactor.ScanProductUseCase
import com.feup.jtp.client_app.util.RSAUtils

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val rsaPublicKey = RSAUtils.loadPublicKey()

    private val qrScanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val scannedData = result.data?.getStringExtra("SCAN_RESULT") ?: result.data?.getStringExtra("qr_result")
            scannedData?.let {
                val encryptedData = it.toByteArray()
                decodeQRCode(encryptedData)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClientappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(innerPadding = innerPadding)
                }
            }
        }
    }

    public fun startScanner() {
        val intent = Intent(this, ScanActivity::class.java)
        qrScanLauncher.launch(intent)
    }

    private fun decodeQRCode(encryptedData: ByteArray) {
        val scanProductUseCase = ScanProductUseCase(rsaPublicKey)
        val product: Product? = scanProductUseCase.execute(encryptedData)

        if (product != null) {
            showScannedResult(product)
        } else {
            showError("Failed to decode the QR code.")
        }
    }

    private fun showScannedResult(product: Product) {
        println("Product decoded: ${product.name} - ${product.price}€ (UUID: ${product.id})")
    }

    private fun showError(message: String) {
        println("Error: $message")
    }
}

@Composable
fun MainScreen(innerPadding: PaddingValues) {
    var scannedResult by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val activity = context as? ComponentActivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Button(
            onClick = { activity?.let { (it as MainActivity).startScanner() } },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Scan QR Code")
        }

        Spacer(modifier = Modifier.height(16.dp))

        scannedResult?.let {
            Text(text = "Scanned result: $it")
        }
    }
}