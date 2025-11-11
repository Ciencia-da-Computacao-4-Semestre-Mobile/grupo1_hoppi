package com.grupo1.hoppi.ui.screens.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

enum class SignUpStep {
    PROFILE_DATA,
    PROFILE_PICTURE
}

@Composable
fun SignUpFlow(
    onLoginClick: () -> Unit,
    onFinish: () -> Unit,
    mockViewModel: SignUpViewModel? = null
) {
    val vm: SignUpViewModel = mockViewModel ?: viewModel()
    var currentStep by remember { mutableStateOf(SignUpStep.PROFILE_DATA) }

    when (currentStep) {
        SignUpStep.PROFILE_DATA -> SignUpStep1Screen(
            onContinue = { currentStep = SignUpStep.PROFILE_PICTURE },
            onLoginClick = onLoginClick,
            viewModel = vm
        )
        SignUpStep.PROFILE_PICTURE -> SignUpStep2Screen(
            onFinish = onFinish
        )
    }
}