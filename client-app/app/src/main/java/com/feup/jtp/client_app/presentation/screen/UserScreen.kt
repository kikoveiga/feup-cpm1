package com.feup.jtp.client_app.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.feup.jtp.client_app.presentation.viewmodel.UserViewModel

@Composable
fun UserScreen(viewModel: UserViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Register User", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.value.name,
            onValueChange = viewModel::onNameChanged,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.value.nickname,
            onValueChange = viewModel::onNicknameChanged,
            label = { Text("Nickname") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.value.paymentCardNumber,
            onValueChange = viewModel::onPaymentCardNumberChanged,
            label = { Text("Card Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.value.expirationDate,
            onValueChange = viewModel::onExpirationDateChanged,
            label = { Text("Expiration Date") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = viewModel::registerUser) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        state.value.user?.let {
            Button(onClick = viewModel::loadTransactions) {
                Text("Load Transactions")
            }

            Spacer(modifier = Modifier.height(8.dp))

            state.value.transactions.forEach { _ ->
                Text("Transaction: ${'$'}{tx.totalPaidCents / 100.0}€")
            }
        }
    }
}