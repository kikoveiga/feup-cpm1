package com.feup.client.presentation.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.feup.client.R
import com.feup.client.domain.model.PaymentCardType
import com.feup.client.presentation.components.MonthYearPicker
import com.feup.client.presentation.components.MyTopAppBar
import com.feup.client.presentation.theme.HighlightOrange
import java.util.Calendar
import java.util.Locale

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoginMode by remember { mutableStateOf(true) }
    var openMonthYearPicker by remember { mutableStateOf(false) }

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

                OutlinedTextField(
                    value = state.value.nickname,
                    onValueChange = { input ->
                        if (!input.contains(" ") && input.length <= 20) {
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
                        if (!input.contains(" ") && input.length <= 20) {
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
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = if (isLoginMode) ImeAction.Done else ImeAction.Next),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }),
                    singleLine = true,
                )

                if (!isLoginMode) {

                    OutlinedTextField(
                        value = state.value.name,
                        onValueChange = { input ->
                            if (!input.contains(" ") && input.length <= 30) {
                                viewModel.onNameChanged(input)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Name") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PaymentCardTypeToggle(
                        selected = state.value.paymentCardType,
                        onSelected = viewModel::onPaymentCardTypeChanged
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
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            openMonthYearPicker = true
                        }),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MonthYearPickerField(
                        selectedDateFormatted = state.value.paymentCardExpirationDate,
                        onDateSelectedFormatted = viewModel::onPaymentCardExpirationDateChanged,
                        showPickerExternally = openMonthYearPicker
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()

                        if (isLoginMode) viewModel.loginUser() else viewModel.registerUser()
                    },
                    enabled = if (isLoginMode) viewModel.isLoginFormValid else viewModel.isRegisterFormValid
                ) {
                    Text(if (isLoginMode) "Login" else "Register")
                }

                TextButton(onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    isLoginMode = !isLoginMode
                }) {
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
    modifier: Modifier = Modifier,
    showPickerExternally: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var showPicker by remember { mutableStateOf(false) }
    var pickedMonth by remember { mutableIntStateOf(-1) }
    var pickedYear by remember { mutableIntStateOf(-1) }

    // Parse MM/YY
    LaunchedEffect(selectedDateFormatted) {
        if (selectedDateFormatted.length == 5) {
            val parts = selectedDateFormatted.split("/")
            pickedMonth = parts[0].toIntOrNull()?.minus(1) ?: Calendar.getInstance().get(Calendar.MONTH)
            pickedYear = ("20" + parts[1]).toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
        }
    }

    // Open externally
    LaunchedEffect(showPickerExternally) {
        if (showPickerExternally) {
            focusManager.clearFocus()
            keyboardController?.hide()
            showPicker = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                focusManager.clearFocus()
                keyboardController?.hide()
                showPicker = true
            }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Expiration Date")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = selectedDateFormatted.ifBlank { "MM/YY" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedDateFormatted.isNotBlank())
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.DateRange, contentDescription = null)
            }
        }
    }

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
fun PaymentCardTypeToggle(
    selected: PaymentCardType?,
    onSelected: (PaymentCardType) -> Unit
) {
    val options = PaymentCardType.entries

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Card Type:",
            modifier = Modifier.padding(end = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        options.forEach { option ->
            val isSelected = selected == option
            val backgroundColor = if (isSelected) HighlightOrange else Color.LightGray
            val contentColor = if (isSelected) Color.White else Color.Black

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .clickable { onSelected(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.name.lowercase().replaceFirstChar { it.uppercase() },
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

