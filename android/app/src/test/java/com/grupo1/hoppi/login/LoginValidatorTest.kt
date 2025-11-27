package com.grupo1.hoppi.login

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginValidatorTest {

    @Test
    fun `email valido deve retornar true`() {
        assertTrue(LoginValidator.isEmailValid("usuario@gmail.com"))
    }

    @Test
    fun `email invalido deve retornar false`() {
        assertFalse(LoginValidator.isEmailValid("usuario-sem-arroba"))
        assertFalse(LoginValidator.isEmailValid("usuario@com"))
    }

    @Test
    fun `senha valida deve ter 6 ou mais caracteres`() {
        assertTrue(LoginValidator.isPasswordValid("123456"))
    }

    @Test
    fun `senha curta deve retornar false`() {
        assertFalse(LoginValidator.isPasswordValid("123"))
    }
}
