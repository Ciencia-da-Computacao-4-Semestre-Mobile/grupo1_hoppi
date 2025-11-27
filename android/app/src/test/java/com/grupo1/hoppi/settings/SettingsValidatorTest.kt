package com.grupo1.hoppi.settings

import org.junit.Assert.*
import org.junit.Test

class SettingsValidatorTest {

    @Test
    fun `todas opcoes verdadeiras retorna true`() {
        val result = SettingsValidator.validateSettingsOptions(true, true, true)
        assertTrue(result)
    }

    @Test
    fun `username falso retorna false`() {
        val result = SettingsValidator.validateSettingsOptions(false, true, true)
        assertFalse(result)
    }

    @Test
    fun `email falso retorna false`() {
        val result = SettingsValidator.validateSettingsOptions(true, false, true)
        assertFalse(result)
    }

    @Test
    fun `password falso retorna false`() {
        val result = SettingsValidator.validateSettingsOptions(true, true, false)
        assertFalse(result)
    }

    @Test
    fun `todas opcoes falsas retorna false`() {
        val result = SettingsValidator.validateSettingsOptions(false, false, false)
        assertFalse(result)
    }

    @Test
    fun `logout confirmado retorna true`() {
        val result = SettingsValidator.validateLogoutConfirmation(true)
        assertTrue(result)
    }

    @Test
    fun `logout nao confirmado retorna false`() {
        val result = SettingsValidator.validateLogoutConfirmation(false)
        assertFalse(result)
    }
}