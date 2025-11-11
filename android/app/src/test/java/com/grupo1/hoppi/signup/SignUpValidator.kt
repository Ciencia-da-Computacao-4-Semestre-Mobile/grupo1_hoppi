package com.grupo1.hoppi.signup

object SignUpValidator {

    fun isNameValid(name: String): Boolean {
        return name.isNotBlank()
    }

    fun isBirthDateValid(birthDate: String): Boolean {
        return birthDate.isNotBlank()
    }

    fun isInstitutionValid(institution: String): Boolean {
        return institution.isNotBlank()
    }

    fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && email.contains("@")
    }

    fun isPasswordValid(password: String): Boolean {
        val minLength = password.length >= 8
        val hasUppercase = password.any { it.isUpperCase() }
        val hasNumber = password.any { it.isDigit() }
        return minLength && hasUppercase && hasNumber
    }

    fun isTermsAccepted(accepted: Boolean): Boolean {
        return accepted
    }

    fun canProceed(
        name: String,
        birthDate: String,
        institution: String,
        email: String,
        password: String,
        acceptedTerms: Boolean
    ): Boolean {
        return isNameValid(name)
                && isBirthDateValid(birthDate)
                && isInstitutionValid(institution)
                && isEmailValid(email)
                && isPasswordValid(password)
                && isTermsAccepted(acceptedTerms)
    }
}