package com.feup.client.presentation.screens.profile

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.feup.client.R
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {

    val state = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val snackbarMessage = state.value?.snackbarMessage
    val snackbarHostState = remember { SnackbarHostState() }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isPasswordVisible by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbarMessage()
        }
    }

    if (state.value == null) {
        CircularProgressIndicator()
        return
    }

    val user = state.value!!.user
    val newPassword = state.value!!.newPassword
    val isNewPasswordValid = newPassword.length >= 4

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {


                Text(text = "Profile", style = typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Name: ${user.name}", style = typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text("Nickname: ${user.nickname}", style = typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { input ->
                        if (!input.contains(" ")) {
                            viewModel.onNewPasswordChanged(input)
                        }
                    },
                    label = { Text("New Password") },
                    trailingIcon = {
                        if (newPassword.isNotBlank()) {
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
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        if (newPassword.isNotBlank() && !isNewPasswordValid) {
                            Text(
                                "Password must be at least 4 characters long",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    isError = newPassword.isNotBlank() && !isNewPasswordValid,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))


                Button(
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        scope.launch { viewModel.updatePassword() }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isNewPasswordValid,
                ) {
                    Text("Update Password")
                }
            }

            Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()) {

                Button(
                    onClick = { viewModel.logoutUser() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log Out")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete Account")
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Account Deletion") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAccount()
                    showDeleteDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}