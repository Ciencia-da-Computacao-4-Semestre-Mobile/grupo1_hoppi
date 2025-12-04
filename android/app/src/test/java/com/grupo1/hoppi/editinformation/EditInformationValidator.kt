package com.grupo1.hoppi.editinformation

object EditInformationValidator {

    fun isValidName(name: String?): Boolean {
        return !name.isNullOrBlank() && name.trim().length >= 3
    }

    fun isValidUsername(username: String?): Boolean {
        if (username.isNullOrBlank()) return false
        if (!username.startsWith("@")) return false
        return username.trim().length >= 4
    }

    fun isValidBirthdate(birthdate: String?): Boolean {
        if (birthdate.isNullOrBlank()) return false
        val regex = Regex("""\d{2}/\d{2}/\d{4}""")
        return regex.matches(birthdate.trim())
    }

    fun isValidInstitution(institution: String?): Boolean {
        return !institution.isNullOrBlank() && institution.trim().length >= 3
    }

    fun canSave(
        newName: String?,
        newUsername: String?,
        newBirthdate: String?,
        newInstitution: String?
    ): Boolean {
        return isValidName(newName)
                && isValidUsername(newUsername)
                && isValidBirthdate(newBirthdate)
                && isValidInstitution(newInstitution)
    }
}
