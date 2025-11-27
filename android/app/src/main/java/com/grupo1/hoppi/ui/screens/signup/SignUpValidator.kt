package com.grupo1.hoppi.ui.screens.signup

object SignUpValidator {

    fun isNameValid(name: String): Boolean {
        return name.isNotBlank() && name.length >= 3
    }

    fun isUsernameValid(username: String): Boolean {
        if (username.isBlank() || username.length < 3) {
            return false
        }
        if (username.length > 30) {
            return false
        }
        val lowerCaseRegex = Regex("^[a-z0-9]+$")

        return username.matches(lowerCaseRegex)
    }

    fun isBirthDateValid(date: String): Boolean {
        return date.matches(Regex("\\d{1,2}/\\d{1,2}/\\d{4}"))
    }

    fun isInstitutionValid(inst: String): Boolean {
        return inst.isNotBlank()
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return email.matches(emailRegex)
    }

    fun isPasswordValid(password: String): Boolean {
        val hasUpper = Regex("[A-Z]").containsMatchIn(password)
        val hasNumber = Regex("[0-9]").containsMatchIn(password)
        return password.length >= 8 && hasUpper && hasNumber
    }

    fun allFieldsValid(
        name: String,
        username: String,
        birthDate: String,
        institution: String,
        email: String,
        password: String,
        acceptedTerms: Boolean
    ): Boolean {
        return isNameValid(name)
                && isUsernameValid(username)
                && isBirthDateValid(birthDate)
                && isInstitutionValid(institution)
                && isEmailValid(email)
                && isPasswordValid(password)
                && acceptedTerms
    }
}