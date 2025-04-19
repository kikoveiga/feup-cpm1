package com.feup.client.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feup.client.domain.usecases.RegisterUserUseCase
import com.feup.client.domain.model.PaymentCard
import com.feup.client.domain.model.PaymentCardType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    val isFormValid: Boolean
        get() = _uiState.value.run {
            name.isNotBlank()
                    && nickname.isNotBlank()
                    && password.isNotBlank()
                    && paymentCardNumber.isNotBlank()
                    && paymentCardExpirationDate.isNotBlank()
        }

    fun onNameChanged(name: String) {
        if (name.length > 30) {
            _uiState.update { it.copy(nameError = "Name too long") }
        } else {
            _uiState.update { it.copy(name = name) }
        }
    }

    fun onNicknameChanged(nickname: String) {
        _uiState.update { it.copy(nickname = nickname) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onPaymentCardTypeChanged(paymentCardType: PaymentCardType) {
        _uiState.update { it.copy(paymentCardType = paymentCardType) }
    }

    fun onPaymentCardNumberChanged(paymentCardNumber: String) {
        _uiState.update { it.copy(paymentCardNumber = paymentCardNumber) }
    }

    fun onPaymentCardExpirationDateChanged(paymentCardExpirationDate: String) {
        _uiState.update { it.copy(paymentCardExpirationDate = paymentCardExpirationDate) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun registerUser() {
        viewModelScope.launch {

            _uiState.update { it.copy(showValidationErrors = true) }
            val result = _uiState.value.let {
                registerUserUseCase.invoke(
                    name = it.name,
                    nickname = it.nickname,
                    password = it.password,
                    paymentCard = PaymentCard(it.paymentCardType, it.paymentCardNumber, it.paymentCardExpirationDate)
                )
            }

            if (result.isSuccess) {
                _uiState.update { RegisterUiState() }
            } else {
                val error = result.exceptionOrNull()
                _uiState.update { it.copy(error = error?.message) }
            }
        }
    }
}