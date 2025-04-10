package com.feup.jtp.client_app

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feup.jtp.client_app.presentation.theme.ClientappTheme

class MainActivity : ComponentActivity() {

    private val qrScanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val scannedData = result.data?.getStringExtra("SCAN_RESULT") ?: result.data?.getStringExtra("qr_result")
            scannedData?.let { showScannedResult(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClientappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(innerPadding = innerPadding)
                    /*
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    */
                }
            }
        }
    }

    public fun startScanner() {
        val intent = Intent(this, ScanActivity::class.java)
        qrScanLauncher.launch(intent)
    }

    private fun showScannedResult(result: String) {
        // Handle displaying the scanned result (e.g., updating UI or adding to basket)
        println("Scanned result: $result") // Placeholder for now
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ClientappTheme {
        Greeting("Android")
    }
}