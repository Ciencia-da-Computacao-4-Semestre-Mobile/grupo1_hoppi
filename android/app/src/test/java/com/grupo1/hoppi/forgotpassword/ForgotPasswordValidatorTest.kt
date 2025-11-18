package com.grupo1.hoppi.forgotpassword

import org.junit.Assert.*
import org.junit.Test

class ForgotPasswordValidatorTest {

    @Test
    fun `email valido retorna true`() {
        assertTrue(ForgotPasswordValidator.validateEmail("teste@exemplo.com"))
    }

    @Test
    fun `email invalido sem arroba retorna false`() {
        assertFalse(ForgotPasswordValidator.validateEmail("email_invalido"))
    }

    @Test
    fun `email vazio retorna false`() {
        assertFalse(ForgotPasswordValidator.validateEmail(""))
    }

    @Test
    fun `email com formato incompleto retorna false`() {
        assertFalse(ForgotPasswordValidator.validateEmail("teste@exemplo"))
    }

    @Test
    fun `codigo valido retorna true`() {
        assertTrue(ForgotPasswordValidator.validateCode("1234"))
    }

    @Test
    fun `codigo com letra retorna false`() {
        assertFalse(ForgotPasswordValidator.validateCode("12a4"))
    }

    @Test
    fun `codigo vazio retorna false`() {
        assertFalse(ForgotPasswordValidator.validateCode(""))
    }

    @Test
    fun `codigo com menos de quatro digitos retorna false`() {
        assertFalse(ForgotPasswordValidator.validateCode("123"))
    }

    @Test
    fun `codigo com mais de quatro digitos retorna false`() {
        assertFalse(ForgotPasswordValidator.validateCode("12345"))
    }

    // NEW PASSWORD
    @Test
    fun `senha valida retorna true`() {
        assertTrue(ForgotPasswordValidator.validateNewPassword("123456", "123456"))
    }

    @Test
    fun `senha curta retorna false`() {
        assertFalse(ForgotPasswordValidator.validateNewPassword("123", "123"))
    }

    @Test
    fun `senhas diferentes retorna false`() {
        assertFalse(ForgotPasswordValidator.validateNewPassword("123456", "654321"))
    }

    @Test
    fun `senha vazia retorna false`() {
        assertFalse(ForgotPasswordValidator.validateNewPassword("", ""))
    }

    @Test
    fun `senha preenchida e confirmacao vazia retorna false`() {
        assertFalse(ForgotPasswordValidator.validateNewPassword("abcdef", ""))
    }

    @Test
    fun `senha aparentemente igual mas com espaco retorna false`() {
        assertFalse(ForgotPasswordValidator.validateNewPassword("123456", "123456 "))
    }
}
