package com.feup.client.presentation.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.feup.client.R
import com.feup.client.domain.model.PaymentCardType
import com.feup.client.presentation.components.MonthYearPicker
import com.feup.client.presentation.components.MyTopAppBar
import java.util.Calendar
import java.util.Locale

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoginMode by remember { mutableStateOf(true) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

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

    Scaffold(
        topBar = { MyTopAppBar() }
    ) { padding ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
            .padding(16.dp)
        ) {

            Column {

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
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }),
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
                            val visibilityIcon =
                                painterResource(id = R.drawable.baseline_visibility_24)
                            val visibilityOffIcon =
                                painterResource(id = R.drawable.baseline_visibility_off_24)

                            Icon(
                                painter = if (isPasswordVisible) visibilityOffIcon else visibilityIcon,
                                contentDescription = "Toggle password visibility",
                                modifier = Modifier.clickable {
                                    isPasswordVisible = !isPasswordVisible
                                }
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }),
                    singleLine = true,
                )

                if (!isLoginMode) {
                    PaymentCardTypeDropdown(
                        state.value.paymentCardType,
                        viewModel::onPaymentCardTypeChanged
                    )

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

                    MonthYearPickerField(
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
    }
}

@Composable
fun MonthYearPickerField(
    selectedDateFormatted: String,
    onDateSelectedFormatted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }
    var pickedMonth by remember { mutableIntStateOf(-1) }
    var pickedYear by remember { mutableIntStateOf(-1) }

    // Parse current MM/YY if provided
    LaunchedEffect(selectedDateFormatted) {
        if (selectedDateFormatted.length == 5) {
            val parts = selectedDateFormatted.split("/")
            pickedMonth = parts[0].toIntOrNull()?.minus(1) ?: Calendar.getInstance().get(Calendar.MONTH)
            pickedYear = ("20" + parts[1]).toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
        }
    }

    OutlinedTextField(
        value = selectedDateFormatted,
        onValueChange = {},
        label = { Text("Expiration Date") },
        placeholder = { Text("MM/YY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select month/year")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showPicker = true
                    }
                }
            },
        readOnly = true,
    )

    if (showPicker) {
        MonthYearPicker(
            visible = true,
            currentMonth = if (pickedMonth != -1) pickedMonth else Calendar.getInstance().get(Calendar.MONTH),
            currentYear = if (pickedYear != -1) pickedYear else Calendar.getInstance().get(Calendar.YEAR),
            onConfirm = { month, year ->
                onDateSelectedFormatted(String.format(Locale.getDefault(), "%02d/%02d", month + 1, year % 100))
                showPicker = false
            },
            onCancel = { showPicker = false }
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

