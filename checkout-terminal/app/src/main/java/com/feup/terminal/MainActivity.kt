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
import com.feup.terminal.presentation.screens.FailureScreen
import com.feup.terminal.presentation.screens.SuccessScreen
import com.feup.terminal.presentation.screens.QRScannerScreen
import com.feup.terminal.presentation.theme.CheckoutTerminalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                CheckoutTerminalTheme {
                    var currentScreen by remember { mutableStateOf("scanner") }

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
                                "scanner" ->
                                    QRScannerScreen(onResult = { success ->
                                    currentScreen = if (success) "success" else "failure"
                                })

                                "success" -> SuccessScreen(onTimeout = {
                                    currentScreen = "scanner"
                                })

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
