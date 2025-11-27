package com.grupo1.hoppi.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo1.hoppi.network.ApiClient
import com.grupo1.hoppi.network.auth.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        _state.update { current ->
            val new = current.copy(name = name)
            updateButtonState(new)
            new
        }
    }

    fun onUsernameChange(username: String) {
        _state.update { current ->
            val new = current.copy(username = username)
            updateButtonState(new)
            new
        }
    }

    fun onBirthDateChange(birthDate: String) {
        _state.update { current ->
            val new = current.copy(birthDate = birthDate)
            updateButtonState(new)
            new
        }
    }

    fun onInstitutionChange(institution: String) {
        _state.update { current ->
            val new = current.copy(institution = institution)
            updateButtonState(new)
            new
        }
    }

    fun onEmailChange(email: String) {
        _state.update { current ->
            val new = current.copy(email = email)
            updateButtonState(new)
            new
        }
    }

    fun onPasswordChange(password: String) {
        _state.update { current ->
            val new = current.copy(password = password)
            updateButtonState(new)
            new
        }
    }

    fun onToggleTerms(accepted: Boolean) {
        _state.update { current ->
            val new = current.copy(acceptedTerms = accepted)
            updateButtonState(new)
            new
        }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun convertToIso(date: String): String {
        val parts = date.split("/")
        val day = parts[0].padStart(2, '0')
        val month = parts[1].padStart(2, '0')
        val year = parts[2]
        return "$year-$month-$day"
    }

    fun submitRegistration(onSuccessAction: () -> Unit) {
        val current = state.value

        viewModelScope.launch {
            println("DEBUG: submitRegistration iniciada.")
            _state.update { it.copy(isLoading = true, errorMessage = null, success = false) }

            try {
                val isoBirthDate = convertToIso(current.birthDate)

                val response = ApiClient.auth.register(
                    RegisterRequest(
                        email = current.email,
                        password = current.password,
                        displayName = current.name,
                        username = current.username,
                        birthDate = isoBirthDate,
                        institution = current.institution,
                    )
                )

                println("DEBUG: Registro bem-sucedido.")
                _state.update { it.copy(isLoading = false, success = true) }
                onSuccessAction()

            } catch (e: Exception) {
                println("DEBUG: Erro na requisição: ${e.message}")
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Erro inesperado."
                    )
                }
            }
        }
    }
}