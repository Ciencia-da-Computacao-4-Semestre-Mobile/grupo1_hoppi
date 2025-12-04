package com.grupo1.hoppi.editinformation

import org.junit.Assert.*
import org.junit.Test

class EditInformationValidatorTest {

    @Test
    fun testValidName() {
        assertTrue(EditInformationValidator.isValidName("Fulano da Silva"))
    }

    @Test
    fun testInvalidNameShort() {
        assertFalse(EditInformationValidator.isValidName("Ca"))
    }

    @Test
    fun testInvalidNameBlank() {
        assertFalse(EditInformationValidator.isValidName("   "))
    }

    @Test
    fun testInvalidNameNull() {
        assertFalse(EditInformationValidator.isValidName(null))
    }

    @Test
    fun testValidUsername() {
        assertTrue(EditInformationValidator.isValidUsername("@fulano"))
    }

    @Test
    fun testInvalidUsernameNoAt() {
        assertFalse(EditInformationValidator.isValidUsername("fulano"))
    }

    @Test
    fun testInvalidUsernameShort() {
        assertFalse(EditInformationValidator.isValidUsername("@a"))
    }

    @Test
    fun testInvalidUsernameBlank() {
        assertFalse(EditInformationValidator.isValidUsername("   "))
    }

    @Test
    fun testInvalidUsernameNull() {
        assertFalse(EditInformationValidator.isValidUsername(null))
    }

    @Test
    fun testValidBirthdate() {
        assertTrue(EditInformationValidator.isValidBirthdate("01/01/2000"))
    }

    @Test
    fun testInvalidBirthdateFormat1() {
        assertFalse(EditInformationValidator.isValidBirthdate("1/1/2000"))
    }

    @Test
    fun testInvalidBirthdateFormat2() {
        assertFalse(EditInformationValidator.isValidBirthdate("2000/01/01"))
    }

    @Test
    fun testInvalidBirthdateFormat3() {
        assertFalse(EditInformationValidator.isValidBirthdate("01-01-2000"))
    }

    @Test
    fun testInvalidBirthdateBlank() {
        assertFalse(EditInformationValidator.isValidBirthdate("   "))
    }

    @Test
    fun testInvalidBirthdateNull() {
        assertFalse(EditInformationValidator.isValidBirthdate(null))
    }

    @Test
    fun testValidInstitution() {
        assertTrue(EditInformationValidator.isValidInstitution("Universidade X"))
    }

    @Test
    fun testInvalidInstitutionShort() {
        assertFalse(EditInformationValidator.isValidInstitution("A"))
    }

    @Test
    fun testInvalidInstitutionBlank() {
        assertFalse(EditInformationValidator.isValidInstitution("  "))
    }

    @Test
    fun testInvalidInstitutionNull() {
        assertFalse(EditInformationValidator.isValidInstitution(null))
    }

    @Test
    fun testCanSaveSuccess() {
        assertTrue(
            EditInformationValidator.canSave(
                "Fulano",
                "@fulano",
                "01/01/2000",
                "Universidade Z"
            )
        )
    }

    @Test
    fun testCanSaveFailureInvalidName() {
        assertFalse(
            EditInformationValidator.canSave(
                "Fa",
                "@fulano",
                "01/01/2000",
                "Universidade Z"
            )
        )
    }

    @Test
    fun testCanSaveFailureInvalidUsername() {
        assertFalse(
            EditInformationValidator.canSave(
                "Fulano",
                "fulano",
                "01/01/2000",
                "Universidade Z"
            )
        )
    }

    @Test
    fun testCanSaveFailureInvalidBirthdate() {
        assertFalse(
            EditInformationValidator.canSave(
                "Fulano",
                "@fulano",
                "2000-01-01",
                "Universidade Z"
            )
        )
    }

    @Test
    fun testCanSaveFailureInvalidInstitution() {
        assertFalse(
            EditInformationValidator.canSave(
                "Fulano",
                "@fulano",
                "01/01/2000",
                ""
            )
        )
    }

    @Test
    fun testCanSaveAllNull() {
        assertFalse(
            EditInformationValidator.canSave(
                null,
                null,
                null,
                null
            )
        )
    }
}