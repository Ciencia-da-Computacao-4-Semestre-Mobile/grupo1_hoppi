package com.grupo1.hoppi.editpassword

import org.junit.Assert.*
import org.junit.Test

class EditPasswordValidatorTest {

    @Test
    fun oldPasswordValid_whenNotEmpty() {
        assertTrue(EditPasswordValidator.isOldPasswordValid("abc"))
    }

    @Test
    fun oldPasswordInvalid_whenEmpty() {
        assertFalse(EditPasswordValidator.isOldPasswordValid(""))
    }

    @Test
    fun newPasswordValid_fullRequirements() {
        assertTrue(EditPasswordValidator.isNewPasswordValid("Abcdefg1"))
    }

    @Test
    fun newPasswordInvalid_whenTooShort() {
        assertFalse(EditPasswordValidator.isNewPasswordValid("Abc1"))
    }

    @Test
    fun newPasswordInvalid_withoutUppercase() {
        assertFalse(EditPasswordValidator.isNewPasswordValid("abcdefg1"))
    }

    @Test
    fun newPasswordInvalid_withoutNumber() {
        assertFalse(EditPasswordValidator.isNewPasswordValid("Abcdefgh"))
    }

    @Test
    fun passwordsMatch_whenEqual() {
        assertTrue(EditPasswordValidator.doPasswordsMatch("Abcdefg1", "Abcdefg1"))
    }

    @Test
    fun passwordsDoNotMatch_whenDifferent() {
        assertFalse(EditPasswordValidator.doPasswordsMatch("Abcdefg1", "Abcdefg2"))
    }

    @Test
    fun recoveryCodeValid_whenCorrect() {
        assertTrue(EditPasswordValidator.isRecoveryCodeValid("1111"))
    }

    @Test
    fun recoveryCodeInvalid_whenWrong() {
        assertFalse(EditPasswordValidator.isRecoveryCodeValid("2222"))
    }
}