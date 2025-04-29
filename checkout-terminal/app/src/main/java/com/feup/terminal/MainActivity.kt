package com.feup.terminal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.feup.terminal.data.model.dto.TransactionFromServerDto
import com.feup.terminal.presentation.screens.FailureScreen
import com.feup.terminal.presentation.screens.SuccessScreen
import com.feup.terminal.presentation.screens.QRScannerScreen
import com.feup.terminal.presentation.theme.CheckoutTerminalTheme
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckoutTerminalTheme {
                var currentScreen by remember { mutableStateOf("scanner") }
                var transactionResult by remember { mutableStateOf<TransactionFromServerDto?>(null) }

                LaunchedEffect(Unit) {
                    try {
                        println("App started successfully")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            "scanner" -> QRScannerScreen(onResult = { result ->
                                if (result != null) {
                                    transactionResult = result
                                    currentScreen = "success"
                                } else {
                                    currentScreen = "failure"
                                }
                            })

                            "success" -> {
                                transactionResult?.let {
                                    SuccessScreen(transactionResult = it, onTimeout = {
                                        currentScreen = "scanner"
                                    })
                                }
                            }

                            "failure" -> FailureScreen(onTimeout = {
                                currentScreen = "scanner"
                            })
                        }
                    }
                }
            }
        }
    }
}
