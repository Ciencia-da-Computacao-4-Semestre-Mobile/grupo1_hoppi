package com.grupo1.hoppi.signup

import com.grupo1.hoppi.ui.screens.signup.SignUpViewModel
import org.junit.Assert.*
import org.junit.Test

class SignUpViewModelTest {

    @Test
    fun testInitialStateButtonDisabled() {
        val vm = SignUpViewModel()
        assertFalse(vm.state.value.isButtonEnabled)
    }

    @Test
    fun testButtonEnabledAfterValidInputs() {
        val vm = SignUpViewModel()

        vm.onNameChange("John Doe")
        vm.onBirthDateChange("10/10/2000")
        vm.onInstitutionChange("University")
        vm.onEmailChange("john@mail.com")
        vm.onPasswordChange("Password1")
        vm.onToggleTerms(true)

        assertTrue(vm.state.value.isButtonEnabled)
    }

    @Test
    fun testButtonDisablesAfterInvalidChange() {
        val vm = SignUpViewModel()

        vm.onNameChange("John Doe")
        vm.onBirthDateChange("10/10/2000")
        vm.onInstitutionChange("University")
        vm.onEmailChange("john@mail.com")
        vm.onPasswordChange("Password1")
        vm.onToggleTerms(true)
        assertTrue(vm.state.value.isButtonEnabled)

        vm.onEmailChange("invalid")
        assertFalse(vm.state.value.isButtonEnabled)
    }

    @Test
    fun testTogglePasswordVisibility() {
        val vm = SignUpViewModel()
        val initial = vm.state.value.isPasswordVisible
        vm.onTogglePasswordVisibility()
        assertNotEquals(initial, vm.state.value.isPasswordVisible)
    }
}
