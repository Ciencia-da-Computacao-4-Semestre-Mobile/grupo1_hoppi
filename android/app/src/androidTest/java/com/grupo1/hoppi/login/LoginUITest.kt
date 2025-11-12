package com.grupo1.hoppi.login

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.grupo1.hoppi.ui.screens.login.LoginScreen
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun loginScreen_buttonsAndFields_workCorrectly() {
        var loginClicked = false
        var forgotPasswordClicked = false
        var signUpClicked = false

        composeTestRule.setContent {
            LoginScreen(
                onForgotPasswordClick = { forgotPasswordClicked = true },
                onSignUpClick = { signUpClicked = true },
                onLoginSuccess = { loginClicked = true }
            )
        }

        composeTestRule.onNodeWithText("Digite seu email")
            .performTextInput("teste@email.com")

        composeTestRule.onNodeWithText("Digite sua senha")
            .performTextInput("123456")

        composeTestRule.onNodeWithText("Entrar").performClick()
        assertTrue(loginClicked)

        composeTestRule.onNodeWithText("Esqueceu a senha?").performClick()
        assertTrue(forgotPasswordClicked)

        composeTestRule.onNodeWithText("Não tem cadastro?").performClick()
        assertTrue(signUpClicked)
    }
}