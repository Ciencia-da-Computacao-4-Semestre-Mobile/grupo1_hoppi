package com.grupo1.hoppi.ui.screens.signup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

open class SignUpViewModel : ViewModel() {

    protected val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state

    private fun updateButtonState(currentState: SignUpState) {
        val isEnabled = SignUpValidator.allFieldsValid(
            name = currentState.name,
            username = currentState.username,
            birthDate = currentState.birthDate,
            institution = currentState.institution,
            email = currentState.email,
            password = currentState.password,
            acceptedTerms = currentState.acceptedTerms
        )

        _state.update { it.copy(isButtonEnabled = isEnabled) }
    }

    fun onNameChange(name: String) {
        _state.update { currentState ->
            val newState = currentState.copy(name = name)
            updateButtonState(newState)
            newState
        }
    }

    fun onUsernameChange(username: String) {
        _state.update { currentState ->
            val newState = currentState.copy(username = username)
            updateButtonState(newState)
            newState
        }
    }

    fun onBirthDateChange(birthDate: String) {
        _state.update { currentState ->
            val newState = currentState.copy(birthDate = birthDate)
            updateButtonState(newState)
            newState
        }
    }

    fun onInstitutionChange(institution: String) {
        _state.update { currentState ->
            val newState = currentState.copy(institution = institution)
            updateButtonState(newState)
            newState
        }
    }

    fun onEmailChange(email: String) {
        _state.update { currentState ->
            val newState = currentState.copy(email = email)
            updateButtonState(newState)
            newState
        }
    }

    fun onPasswordChange(password: String) {
        _state.update { currentState ->
            val newState = currentState.copy(password = password)
            updateButtonState(newState)
            newState
        }
    }

    fun onToggleTerms(accepted: Boolean) {
        _state.update { currentState ->
            val newState = currentState.copy(acceptedTerms = accepted)
            updateButtonState(newState)
            newState
        }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
}
