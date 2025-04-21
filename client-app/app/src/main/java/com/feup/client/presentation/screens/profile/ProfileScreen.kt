package com.feup.client.presentation.screens.profile

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()

    Button(onClick = { viewModel.logoutUser() }) {
        Text("Log out User")
    }
}