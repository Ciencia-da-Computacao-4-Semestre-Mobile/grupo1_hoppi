package com.grupo1.hoppi.editpassword

object EditPasswordValidator {

    fun isOldPasswordValid(old: String): Boolean {
        return old.isNotBlank()
    }

    fun isNewPasswordValid(new: String): Boolean {
        if (new.length < 8) return false
        if (!new.any { it.isUpperCase() }) return false
        if (!new.any { it.isDigit() }) return false
        return true
    }

    fun doPasswordsMatch(new: String, confirm: String): Boolean {
        return new == confirm
    }

    fun isRecoveryCodeValid(code: String): Boolean {
        return code == "1111"
    }
}
