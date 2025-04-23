package com.feup.client.presentation.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.feup.client.R
import com.feup.client.domain.model.PaymentCardType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoginMode by remember { mutableStateOf(false) }

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

        if (!isLoginMode) {
            OutlinedTextField(
                value = state.value.name,
                onValueChange = viewModel::onNameChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Name") },
                supportingText = {
                    state.value.nameError?.let {
                        Text(it)
                    }
                },
                isError = state.value.nameError != null,
                singleLine = true,
            )
        }

        OutlinedTextField(
            value = state.value.nickname,
            onValueChange = { input ->
                if (!input.contains(" ")) {
                    viewModel.onNicknameChanged(input)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nickname") },
            singleLine = true,
        )

        OutlinedTextField(
            value = state.value.password,
            onValueChange = { input ->
                if (!input.contains(" ")) {
                    viewModel.onPasswordChanged(input)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            trailingIcon = {
                if (state.value.password.isNotBlank()) {
                    val visibilityIcon = painterResource(id = R.drawable.baseline_visibility_24)
                    val visibilityOffIcon = painterResource(id = R.drawable.baseline_visibility_off_24)

                    Icon(
                        painter = if (isPasswordVisible) visibilityOffIcon else visibilityIcon,
                        contentDescription = "Toggle password visibility",
                        modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible }
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
        )

        if (!isLoginMode) {
            PaymentCardTypeDropdown(state.value.paymentCardType, viewModel::onPaymentCardTypeChanged)

            OutlinedTextField(
                value = state.value.paymentCardNumber,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } && input.length <= 16) {
                        viewModel.onPaymentCardNumberChanged(input)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Card Number") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
            )

            DatePickerFieldToModal(
                selectedDateFormatted = state.value.paymentCardExpirationDate,
                onDateSelectedFormatted = viewModel::onPaymentCardExpirationDateChanged
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isLoginMode) viewModel.loginUser()
                else viewModel.registerUser()
            },
        ) {
            Text(if (isLoginMode) "Login" else "Register")
        }

        TextButton(onClick = { isLoginMode = !isLoginMode }) {
            Text(if (isLoginMode) "Don't have an account? Register" else "Already have an account? Login")
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

@Composable
fun PaymentCardTypeDropdown(
    selectedPaymentCardType: PaymentCardType?,
    onPaymentCardTypeSelected: (PaymentCardType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedPaymentCardType.toString(),
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        label = { Text("Card Type") },
        trailingIcon = {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            }
        },
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        PaymentCardType.entries.forEach { paymentCardType ->
            DropdownMenuItem(
                text = { Text(paymentCardType.toString()) },
                onClick = {
                    onPaymentCardTypeSelected(paymentCardType)
                    expanded = false
                }
            )
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

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/yy", Locale.getDefault())
    return formatter.format(Date(millis))
}
