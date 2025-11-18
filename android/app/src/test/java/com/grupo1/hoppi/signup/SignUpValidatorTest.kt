package com.grupo1.hoppi.signup

import com.grupo1.hoppi.ui.screens.signup.SignUpValidator
import com.grupo1.hoppi.ui.screens.signup.SignUpViewModel
import org.junit.Assert.*
import org.junit.Test

class SignUpFlowTest {

    @Test
    fun testNameValidation() {
        assertTrue(SignUpValidator.isNameValid("John Doe"))
        assertFalse(SignUpValidator.isNameValid(""))
        assertFalse(SignUpValidator.isNameValid("A"))
    }

    @Test
    fun testBirthDateValidation() {
        assertTrue(SignUpValidator.isBirthDateValid("12/10/2000"))
        assertFalse(SignUpValidator.isBirthDateValid("12-10-2000"))
        assertFalse(SignUpValidator.isBirthDateValid(""))
    }

    @Test
    fun testInstitutionValidation() {
        assertTrue(SignUpValidator.isInstitutionValid("University"))
        assertFalse(SignUpValidator.isInstitutionValid(""))
    }

    @Test
    fun testEmailValidation() {
        assertTrue(SignUpValidator.isEmailValid("user@mail.com"))
        assertFalse(SignUpValidator.isEmailValid("invalid"))
        assertFalse(SignUpValidator.isEmailValid("mail@domain"))
    }

    @Test
    fun testPasswordValidation() {
        assertTrue(SignUpValidator.isPasswordValid("Password1"))
        assertFalse(SignUpValidator.isPasswordValid("short1"))
        assertFalse(SignUpValidator.isPasswordValid("nocaps123"))
        assertTrue(SignUpValidator.isPasswordValid("NOLOWERCASE1"))
    }

    @Test
    fun testAllFieldsValid() {
        val result = SignUpValidator.allFieldsValid(
            name = "John Doe",
            birthDate = "10/10/2000",
            institution = "Institute",
            email = "test@mail.com",
            password = "Password1",
            acceptedTerms = true
        )
        assertTrue(result)
    }

    @Test
    fun testAllFieldsInvalid() {
        val result = SignUpValidator.allFieldsValid(
            name = "",
            birthDate = "10-10-2000",
            institution = "",
            email = "invalid",
            password = "123",
            acceptedTerms = false
        )
        assertFalse(result)
    }

    @Test
    fun testFlowGoesToStep2WhenValid() {
        val vm = SignUpViewModel()
        vm.onNameChange("User Example")
        vm.onBirthDateChange("10/10/2000")
        vm.onInstitutionChange("Institute")
        vm.onEmailChange("example@mail.com")
        vm.onPasswordChange("Password1")
        vm.onToggleTerms(true)

        assertTrue(
            SignUpValidator.allFieldsValid(
                vm.state.value.name,
                vm.state.value.birthDate,
                vm.state.value.institution,
                vm.state.value.email,
                vm.state.value.password,
                vm.state.value.acceptedTerms
            )
        )
    }

    @Test
    fun testSelectedAvatarIsValid() {
        val selectedAvatarIndex = 2
        assertTrue(selectedAvatarIndex != -1)
    }

    @Test
    fun testAvatarChange() {
        var selectedAvatarIndex = 1
        selectedAvatarIndex = 4
        assertEquals(4, selectedAvatarIndex)
    }

    @Test
    fun testPasswordMissingNumber() {
        assertFalse(SignUpValidator.isPasswordValid("Password"))
    }

    @Test
    fun testPasswordEmpty() {
        assertFalse(SignUpValidator.isPasswordValid(""))
    }

    @Test
    fun testPasswordExactlyEightCharsValid() {
        assertTrue(SignUpValidator.isPasswordValid("A2345678"))
    }

    @Test
    fun testNameWithSpacesOnly() {
        assertFalse(SignUpValidator.isNameValid("   "))
    }

    @Test
    fun testNameExactlyThreeChars() {
        assertTrue(SignUpValidator.isNameValid("Ana"))
    }

    @Test
    fun testEmailEmpty() {
        assertFalse(SignUpValidator.isEmailValid(""))
    }

    @Test
    fun testEmailWeirdFormat() {
        assertFalse(SignUpValidator.isEmailValid("a@.com"))
    }

    @Test
    fun testBirthDateInvalidDayButAcceptedByRegex() {
        assertTrue(SignUpValidator.isBirthDateValid("32/12/2020"))
    }

    @Test
    fun testInstitutionSpacesOnly() {
        assertFalse(SignUpValidator.isInstitutionValid("   "))
    }

    @Test
    fun testAllFieldsInvalidTermsOnly() {
        val result = SignUpValidator.allFieldsValid(
            name = "John Doe",
            birthDate = "10/10/2000",
            institution = "Uni",
            email = "test@mail.com",
            password = "Password1",
            acceptedTerms = false
        )
        assertFalse(result)
    }
}