package com.feup.client.presentation.screens.shopping

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.feup.client.presentation.screens.register.RegisterViewModel

@Composable
fun ShoppingScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    Text("shopping")
}