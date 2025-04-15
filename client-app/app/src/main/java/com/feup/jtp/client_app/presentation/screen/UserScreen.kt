package com.feup.jtp.client_app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.feup.jtp.client_app.presentation.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserScreen(viewModel: UserViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (state.value.error != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            },
            title = { Text("Error") },
            text = { Text(state.value.error!!) }
        )
    }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { millis ->
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = millis?.let { formatter.format(Date(it)) } ?: ""
                viewModel.onExpirationDateChanged(formattedDate)
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Register User", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.value.name,
            onValueChange = viewModel::onNameChanged,
            label = { Text("Name") },
            isError = state.value.showValidationErrors && state.value.name.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.value.nickname,
            onValueChange = viewModel::onNicknameChanged,
            label = { Text("Nickname") },
            isError = state.value.showValidationErrors && state.value.nickname.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.value.paymentCardNumber,
            onValueChange = viewModel::onPaymentCardNumberChanged,
            label = { Text("Card Number") },
            isError = state.value.showValidationErrors && state.value.paymentCardNumber.length < 8,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.value.expirationDate,
            onValueChange = {},
            label = { Text("Expiration Date") },
            isError = state.value.showValidationErrors && state.value.expirationDate.length < 5,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            readOnly = true
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

            state.value.transactions.forEach { transaction ->
                Text("Transaction: ${transaction.price}€")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
