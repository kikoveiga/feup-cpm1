package com.feup.client

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.feup.client.di.UserDataStoreEntryPoint
import com.feup.client.presentation.navigation.AppScaffold
import com.feup.client.presentation.screens.register.RegisterScreen
import com.feup.client.presentation.theme.ClientappTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClientappTheme {
                val context = LocalContext.current
                var isRegistered by remember { mutableStateOf<Boolean?>(null) }
                val userDataStore = remember {
                    EntryPointAccessors.fromApplication(
                        context.applicationContext,
                        UserDataStoreEntryPoint::class.java
                    ).userDataStore()
                }

                LaunchedEffect(true) {
                    isRegistered = userDataStore.isRegistered()
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