package com.grupo1.hoppi.forgotpassword

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ForgotPasswordValidatorTest {

    @Test
    fun forgotPassword_flow_transitionsCorrectly() {
        var backToLoginCalled = false

        val flow = ForgotPasswordValidator(
            onBackToLogin = { backToLoginCalled = true }
        )

        assertEquals(RecoveryStep.SELECT_METHOD, flow.currentStep)

        flow.codeSent()
        assertEquals(RecoveryStep.VERIFY_CODE, flow.currentStep)

        flow.verificationSuccess()
        assertEquals(RecoveryStep.SET_NEW_PASSWORD, flow.currentStep)

        flow.passwordChangeSuccess()
        assertTrue(backToLoginCalled)
    }

    @Test
    fun forgotPassword_flow_retryWorks() {
        val flow = ForgotPasswordValidator(
            onBackToLogin = {}
        )

        flow.codeSent()
        assertEquals(RecoveryStep.VERIFY_CODE, flow.currentStep)

        flow.retry()
        assertEquals(RecoveryStep.SELECT_METHOD, flow.currentStep)
    }

    @Test
    fun forgotPassword_flow_doesNothing_whenCalledInWrongStep() {
        val flow = ForgotPasswordValidator(onBackToLogin = {})

        flow.codeSent()
        flow.codeSent()
        assertEquals(RecoveryStep.VERIFY_CODE, flow.currentStep)

        flow.verificationSuccess()
        flow.verificationSuccess()
        assertEquals(RecoveryStep.SET_NEW_PASSWORD, flow.currentStep)

        flow.retry()
        assertEquals(RecoveryStep.SET_NEW_PASSWORD, flow.currentStep)

        var backToLoginCalled = false
        val flow2 = ForgotPasswordValidator { backToLoginCalled = true }

        flow2.passwordChangeSuccess()
        assertFalse(backToLoginCalled)
        assertEquals(RecoveryStep.SELECT_METHOD, flow2.currentStep)
    }
}