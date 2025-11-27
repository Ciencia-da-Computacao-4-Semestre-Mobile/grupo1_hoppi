package com.grupo1.hoppi.editemail

import org.junit.Assert.*
import org.junit.Test

class EditEmailValidatorTest {

    @Test
    fun testValidEmail() {
        assertTrue(EditEmailValidator.isValidEmail("teste@gmail.com"))
    }

    @Test
    fun testInvalidEmailNoAt() {
        assertFalse(EditEmailValidator.isValidEmail("teste.gmail.com"))
    }

    @Test
    fun testInvalidEmailBlank() {
        assertFalse(EditEmailValidator.isValidEmail("   "))
    }

    @Test
    fun testInvalidEmailNull() {
        assertFalse(EditEmailValidator.isValidEmail(null))
    }

    @Test
    fun testDifferentEmail() {
        assertTrue(EditEmailValidator.isDifferentEmail("a@a.com", "b@b.com"))
    }

    @Test
    fun testDifferentEmailBothNull() {
        assertFalse(EditEmailValidator.isDifferentEmail(null, null))
    }

    @Test
    fun testSameEmail() {
        assertFalse(EditEmailValidator.isDifferentEmail("a@a.com", "a@a.com"))
    }

    @Test
    fun testDifferentEmailNull() {
        assertFalse(EditEmailValidator.isDifferentEmail(null, "a@a.com"))
        assertFalse(EditEmailValidator.isDifferentEmail("a@a.com", null))
    }

    @Test
    fun testValidCode() {
        assertTrue(EditEmailValidator.isValidCode("1234"))
    }

    @Test
    fun testInvalidCodeLetters() {
        assertFalse(EditEmailValidator.isValidCode("12a4"))
    }

    @Test
    fun testInvalidCodeShort() {
        assertFalse(EditEmailValidator.isValidCode("12"))
    }

    @Test
    fun testInvalidCodeBlank() {
        assertFalse(EditEmailValidator.isValidCode("   "))
    }

    @Test
    fun testInvalidCodeNull() {
        assertFalse(EditEmailValidator.isValidCode(null))
    }

    @Test
    fun testCanSendEmailSuccess() {
        assertTrue(EditEmailValidator.canSendEmail("old@mail.com", "new@mail.com"))
    }

    @Test
    fun testCanSendEmailFailInvalidEmail() {
        assertFalse(EditEmailValidator.canSendEmail("old@mail.com", "newmail.com"))
    }

    @Test
    fun testCanSendEmailFailSameEmail() {
        assertFalse(EditEmailValidator.canSendEmail("igual@mail.com", "igual@mail.com"))
    }

    @Test
    fun testCanSendEmailFailCurrentNull() {
        assertFalse(EditEmailValidator.canSendEmail(null, "novo@email.com"))
    }

    @Test
    fun testCanValidateCodeSuccess() {
        assertTrue(EditEmailValidator.canValidateCode("1234", "1234"))
    }

    @Test
    fun testCanValidateCodeFailWrongCode() {
        assertFalse(EditEmailValidator.canValidateCode("1234", "9999"))
    }

    @Test
    fun testCanValidateCodeFailInvalidCode() {
        assertFalse(EditEmailValidator.canValidateCode("12", "1234"))
    }

    @Test
    fun testCanValidateCodeFailExpectedInvalid() {
        assertFalse(EditEmailValidator.canValidateCode("12a4", "12a4"))
    }
}