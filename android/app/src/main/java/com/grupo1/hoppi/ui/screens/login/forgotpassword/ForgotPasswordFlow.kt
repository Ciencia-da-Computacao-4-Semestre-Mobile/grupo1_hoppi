package com.grupo1.hoppi.ui.screens.login.forgotpassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

enum class RecoveryStep {
    SELECT_METHOD,
    VERIFY_CODE,
    SET_NEW_PASSWORD
}

@Composable
fun ForgotPasswordFlow(onBackToLogin: () -> Unit) {
    var currentStep by remember { mutableStateOf(RecoveryStep.SELECT_METHOD) }

    when (currentStep) {
        RecoveryStep.SELECT_METHOD -> RecoveryStep1Screen(
            onCodeSent = { currentStep = RecoveryStep.VERIFY_CODE },
            onBack = onBackToLogin
        )
        RecoveryStep.VERIFY_CODE -> RecoveryStep2Screen(
            onVerificationSuccess = { currentStep = RecoveryStep.SET_NEW_PASSWORD },
            onRetry = { currentStep = RecoveryStep.SELECT_METHOD }
        )
        RecoveryStep.SET_NEW_PASSWORD -> RecoveryStep3Screen(
            onPasswordChangeSuccess = onBackToLogin
        )
    }
}