package com.feup.client.presentation.screens.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TransactionsScreen(viewModel: TransactionsViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Button(onClick = {  }) {
            Text("Load transactions")
        }
        Spacer(modifier = Modifier.height(16.dp))

        state.value.transactions.forEach { transaction ->
            Text("${transaction.date} - ${transaction.price}€")
        }
    }
}