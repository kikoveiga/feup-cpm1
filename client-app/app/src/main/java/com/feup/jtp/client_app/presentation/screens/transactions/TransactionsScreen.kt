package com.feup.jtp.client_app.presentation.screens.transactions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.feup.jtp.client_app.presentation.screens.register.RegisterViewModel

@Composable
fun TransactionsScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    Text("transactions")
}