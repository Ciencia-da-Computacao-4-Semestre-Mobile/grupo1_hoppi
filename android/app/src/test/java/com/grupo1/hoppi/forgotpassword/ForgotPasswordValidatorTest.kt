package com.grupo1.hoppi.forgotpassword

import org.junit.Assert.*
import org.junit.Test

class ForgotPasswordValidatorTest {

    @Test
    fun `email valido retorna true`() {
        val result = ForgotPasswordValidator.validateEmail("teste@exemplo.com")
        assertTrue(result)
    }

    @Test
    fun `email invalido retorna false`() {
        val result = ForgotPasswordValidator.validateEmail("email_invalido")
        assertFalse(result)
    }

    @Test
    fun `codigo valido retorna true`() {
        val result = ForgotPasswordValidator.validateCode("1234")
        assertTrue(result)
    }

    @Test
    fun `codigo invalido retorna false`() {
        val result = ForgotPasswordValidator.validateCode("12a4")
        assertFalse(result)
    }

    @Test
    fun `senha valida retorna true`() {
        val result = ForgotPasswordValidator.validateNewPassword("123456", "123456")
        assertTrue(result)
    }

    @Test
    fun `senha curta retorna false`() {
        val result = ForgotPasswordValidator.validateNewPassword("123", "123")
        assertFalse(result)
    }

    @Test
    fun `senhas diferentes retorna false`() {
        val result = ForgotPasswordValidator.validateNewPassword("123456", "654321")
        assertFalse(result)
    }
}
