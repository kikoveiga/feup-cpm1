package com.feup.client.presentation.screens.profile

import com.feup.client.domain.model.User

data class ProfileUiState (
    val user: User,
    val newPassword: String = "",
    val snackbarMessage: String? = null,
    val error: String? = null,
)