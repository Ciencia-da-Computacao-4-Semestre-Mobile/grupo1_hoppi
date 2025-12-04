package com.grupo1.hoppi.signup

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.grupo1.hoppi.ui.screens.signup.SignUpFlow
import com.grupo1.hoppi.ui.screens.signup.SignUpViewModel
import org.junit.Rule
import org.junit.Test

class SignUpUITest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testFullSignUpFlow() {
        var finished = false
        val viewModel = SignUpViewModel()

        rule.setContent {
            SignUpFlow(
                onLoginClick = {},
                onFinish = { finished = true },
                mockViewModel = viewModel
            )
        }

        rule.onNodeWithText("Digite seu nome completo").performTextInput("João Silva")
        rule.onNodeWithText("Digite seu nome de usuário").performTextInput("joaosilva123")

        rule.runOnIdle {
            viewModel.onBirthDateChange("10/05/2003")
            viewModel.onToggleTerms(true)
        }
        rule.waitForIdle()

        rule.onNodeWithText("Digite sua instituição de ensino").performTextInput("Universidade XPTO")
        rule.onNodeWithText("Digite seu e-mail").performTextInput("teste@example.com")
        rule.onNodeWithText("Digite sua senha").performTextInput("Abc12345")

        rule.runOnIdle {
            check(viewModel.state.value.isButtonEnabled) {
                "Botão não ativou! Estado: ${viewModel.state.value}"
            }
        }

        rule.onNodeWithTag("SignUpButton")
            .performScrollTo()
            .assertIsEnabled()
            .performClick()

        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag("SignUpStep2").fetchSemanticsNodes().isNotEmpty()
        }

        rule.onNodeWithTag("Avatar1")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        rule.onNodeWithTag("FinishSignUp")
            .performScrollTo()
            .assertIsEnabled()
            .performClick()

        assert(finished)
    }
}
