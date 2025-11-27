package com.grupo1.hoppi.ui.screens.login.forgotpassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.grupo1.hoppi.ui.screens.login.auth.ForgotPasswordViewModel

enum class RecoveryStep {
    SELECT_METHOD,
    VERIFY_CODE,
    SET_NEW_PASSWORD
}

@Composable
fun ForgotPasswordFlow(onBackToLogin: () -> Unit) {
    val viewModel: ForgotPasswordViewModel = viewModel()

    var currentStep by remember { mutableStateOf(RecoveryStep.SELECT_METHOD) }

    when (currentStep) {
        RecoveryStep.SELECT_METHOD -> RecoveryStep1Screen(
            viewModel = viewModel,
            onCodeSent = { currentStep = RecoveryStep.VERIFY_CODE },
            onBack = onBackToLogin
        )
        RecoveryStep.VERIFY_CODE -> RecoveryStep2Screen(
            viewModel = viewModel,
            onVerificationSuccess = { currentStep = RecoveryStep.SET_NEW_PASSWORD },
            onRetry = { currentStep = RecoveryStep.SELECT_METHOD }
        )
        RecoveryStep.SET_NEW_PASSWORD -> RecoveryStep3Screen(
            viewModel = viewModel,
            onPasswordChangeSuccess = onBackToLogin
        )
    }
}

