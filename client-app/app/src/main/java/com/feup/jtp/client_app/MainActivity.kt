package com.feup.jtp.client_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.feup.jtp.client_app.presentation.screen.UserScreen
import com.feup.jtp.client_app.presentation.theme.ClientappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClientappTheme {
                UserScreen()
            }
        }
    }
}