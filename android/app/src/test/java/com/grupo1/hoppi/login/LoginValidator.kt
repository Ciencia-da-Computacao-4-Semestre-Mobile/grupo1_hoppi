package com.grupo1.hoppi.login

object LoginValidator {
    fun isEmailValid(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}