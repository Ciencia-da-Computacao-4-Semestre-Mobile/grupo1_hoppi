package com.grupo1.hoppi.ui.screens.signup

data class SignUpState(
    val name: String = "",
    val birthDate: String = "",
    val institution: String = "",
    val email: String = "",
    val password: String = "",
    val acceptedTerms: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isButtonEnabled: Boolean = false
)