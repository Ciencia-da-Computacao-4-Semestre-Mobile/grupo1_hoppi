package com.grupo1.hoppi.settings

object SettingsValidator {
    fun validateSettingsOptions(
        hasUsername: Boolean,
        hasEmail: Boolean,
        hasPassword: Boolean
    ): Boolean {
        return hasUsername && hasEmail && hasPassword
    }

    fun validateLogoutConfirmation(confirm: Boolean): Boolean {
        return confirm
    }
}