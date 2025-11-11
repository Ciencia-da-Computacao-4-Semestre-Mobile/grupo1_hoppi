package com.grupo1.hoppi.login

enum class RecoveryStep {
    SELECT_METHOD,
    VERIFY_CODE,
    SET_NEW_PASSWORD
}

class ForgotPasswordValidator(
    private val onBackToLogin: () -> Unit
) {
    var currentStep: RecoveryStep = RecoveryStep.SELECT_METHOD
        private set

    fun codeSent() {
        if (currentStep == RecoveryStep.SELECT_METHOD) {
            currentStep = RecoveryStep.VERIFY_CODE
        }
    }

    fun verificationSuccess() {
        if (currentStep == RecoveryStep.VERIFY_CODE) {
            currentStep = RecoveryStep.SET_NEW_PASSWORD
        }
    }

    fun passwordChangeSuccess() {
        if (currentStep == RecoveryStep.SET_NEW_PASSWORD) {
            onBackToLogin()
        }
    }

    fun retry() {
        if (currentStep == RecoveryStep.VERIFY_CODE) {
            currentStep = RecoveryStep.SELECT_METHOD
        }
    }
}
