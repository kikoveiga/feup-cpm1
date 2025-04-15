package com.feup.jtp.client_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.feup.jtp.client_app.data.local.UserPrefs
import com.feup.jtp.client_app.presentation.navigation.AppScaffold
import com.feup.jtp.client_app.presentation.screens.register.RegisterScreen
import com.feup.jtp.client_app.presentation.theme.ClientappTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClientappTheme {
                val context = LocalContext.current
                var isRegistered by remember { mutableStateOf<Boolean?>(null) }

                LaunchedEffect(true) {
                    isRegistered = UserPrefs.isRegistered(context)
                }

                when (isRegistered) {
                    true -> AppScaffold()
                    false -> RegisterScreen(onRegistered = {
                        isRegistered = true
                    })
                    null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }

            }
        }
    }
}