package com.grupo1.hoppi.forgotpassword

object ForgotPasswordValidator {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    fun validateEmail(email: String): Boolean {
        return email.isNotBlank() && emailRegex.matches(email)
    }

    fun validateCode(code: String): Boolean {
        return code.length == 4 && code.all { it.isDigit() }
    }

    fun validateNewPassword(password: String, confirmPassword: String): Boolean {
        return password.length >= 6 && password == confirmPassword
    }
}