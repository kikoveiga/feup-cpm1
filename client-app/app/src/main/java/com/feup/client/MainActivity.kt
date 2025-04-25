package com.feup.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.feup.client.di.UserDataStoreEntryPoint
import com.feup.client.presentation.navigation.AppScaffold
import com.feup.client.presentation.screens.auth.AuthScreen
import com.feup.client.presentation.theme.ClientTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
                ClientTheme {
                    val context = LocalContext.current
                    val userDataStore = remember {
                        EntryPointAccessors.fromApplication(
                            context.applicationContext,
                            UserDataStoreEntryPoint::class.java
                        ).userDataStore()
                    }

                    val isAnyUserLoggedIn by userDataStore.isLoggedInFlow.collectAsState(initial = null)

                    when (isAnyUserLoggedIn) {
                        true -> AppScaffold()
                        false -> AuthScreen()
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