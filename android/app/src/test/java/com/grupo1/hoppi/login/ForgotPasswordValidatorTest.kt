package com.grupo1.hoppi.login

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ForgotPasswordFlowLogicTest {

    @Test
    fun forgotPassword_flow_transitionsCorrectly() {
        var backToLoginCalled = false

        val flow = ForgotPasswordFlowLogic(
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
        val flow = ForgotPasswordFlowLogic(
            onBackToLogin = {}
        )

        flow.codeSent()
        assertEquals(RecoveryStep.VERIFY_CODE, flow.currentStep)

        flow.retry()
        assertEquals(RecoveryStep.SELECT_METHOD, flow.currentStep)
    }
}