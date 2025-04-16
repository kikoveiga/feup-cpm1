package com.feup.client.presentation.screens.register

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RegisterScreen(onRegistered: () -> Unit, viewModel: RegisterViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()

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

        DatePickerFieldToModal(
            selectedDateFormatted = state.value.paymentCardExpirationDate,
            onDateSelectedFormatted = viewModel::onPaymentCardExpirationDateChanged
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.registerUser(onRegistered = onRegistered) }) {
            Text("Register")
        }
    }
}

@Composable
fun DatePickerFieldToModal(selectedDateFormatted: String, onDateSelectedFormatted: (String) -> Unit, modifier: Modifier = Modifier) {
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDateFormatted,
        onValueChange = { },
        label = { Text("Expiration Date") },
        placeholder = { Text("MM/YY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            },
        readOnly = true
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                val formatted = it?.let { convertMillisToDate(it) } ?: ""
                onDateSelectedFormatted(formatted)
            },
            onDismiss = { showModal = false }
        )
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
        DatePicker(
            state = datePickerState
        )
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/yy", Locale.getDefault())
    return formatter.format(Date(millis))
}