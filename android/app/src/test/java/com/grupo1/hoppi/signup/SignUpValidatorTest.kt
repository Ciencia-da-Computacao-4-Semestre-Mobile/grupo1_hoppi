package com.grupo1.hoppi.signup

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SignUpValidatorTest {

    @Test
    fun `valid input passes validation`() {
        val canProceed = SignUpValidator.canProceed(
            name = "Nome Usuário",
            birthDate = "10/11/2003",
            institution = "UCLA",
            email = "email@example.com",
            password = "Senha123",
            acceptedTerms = true
        )
        assertTrue(canProceed)
    }

    @Test
    fun `empty name fails validation`() {
        val canProceed = SignUpValidator.canProceed(
            name = "",
            birthDate = "10/11/2003",
            institution = "UCLA",
            email = "email@example.com",
            password = "Senha123",
            acceptedTerms = true
        )
        assertFalse(canProceed)
    }

    @Test
    fun `invalid email fails validation`() {
        val canProceed = SignUpValidator.canProceed(
            name = "Nome Usuário",
            birthDate = "10/11/2003",
            institution = "UCLA",
            email = "emailexample.com",
            password = "Senha123",
            acceptedTerms = true
        )
        assertFalse(canProceed)
    }

    @Test
    fun `weak password fails validation`() {
        val canProceed = SignUpValidator.canProceed(
            name = "Nome Usuário",
            birthDate = "10/11/2003",
            institution = "UCLA",
            email = "email@example.com",
            password = "senha",
            acceptedTerms = true
        )
        assertFalse(canProceed)
    }

    @Test
    fun `terms not accepted fails validation`() {
        val canProceed = SignUpValidator.canProceed(
            name = "Nome Usuário",
            birthDate = "10/11/2003",
            institution = "UCLA",
            email = "email@example.com",
            password = "Senha123",
            acceptedTerms = false
        )
        assertFalse(canProceed)
    }
}