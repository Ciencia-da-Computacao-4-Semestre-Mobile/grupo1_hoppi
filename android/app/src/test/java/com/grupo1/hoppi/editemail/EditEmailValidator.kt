package com.grupo1.hoppi.editemail

import android.util.Patterns

object EditEmailValidator {

    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return regex.matches(email)
    }

    fun isDifferentEmail(current: String?, new: String?): Boolean {
        if (current.isNullOrBlank() || new.isNullOrBlank()) return false
        return !current.equals(new, ignoreCase = true)
    }

    fun isValidCode(code: String?): Boolean {
        if (code.isNullOrBlank()) return false
        if (code.length != 4) return false
        return code.all { it.isDigit() }
    }

    fun canSendEmail(current: String?, new: String?): Boolean {
        return isValidEmail(new) && isDifferentEmail(current, new)
    }

    fun canValidateCode(code: String?, correctCode: String): Boolean {
        return isValidCode(code) && code == correctCode
    }
}
