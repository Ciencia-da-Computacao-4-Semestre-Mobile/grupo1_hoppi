package com.grupo1.hoppi

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test

class MainActivityUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun loginScreen_isDisplayedInitially() {
        composeTestRule.onNodeWithText("Entrar", ignoreCase = true, useUnmergedTree = true).assertExists()
    }

    @Test
    fun navigate_toForgotPassword_andBack() {
        composeTestRule.onNodeWithText("Esqueceu a senha?", ignoreCase = true, useUnmergedTree = true).performClick()
        composeTestRule.waitUntilExists(hasText("Esqueceu a senha?", ignoreCase = true))
        composeTestRule.onNodeWithText("Voltar", ignoreCase = true, useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("Entrar", ignoreCase = true, useUnmergedTree = true).assertExists()
    }

    @Test
    fun navigate_toSignUp_andBack() {
        composeTestRule.waitUntilExists(hasText("Não tem cadastro?", ignoreCase = true))
        composeTestRule.onNodeWithText("Não tem cadastro?", ignoreCase = true, useUnmergedTree = true).performClick()
        composeTestRule.waitUntilExists(hasText("Criar conta", ignoreCase = true))
        composeTestRule.onNodeWithText("Já tenho uma conta", ignoreCase = true, useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("Entrar", ignoreCase = true, useUnmergedTree = true).assertExists()
    }

    @Test
    fun navigate_toMainApp_afterLogin() {
        composeTestRule.onNodeWithText("Entrar", ignoreCase = true, useUnmergedTree = true).performClick()
        composeTestRule.waitUntilExists(hasContentDescription("Perfil", ignoreCase = true))
        composeTestRule.onNodeWithText("Perfil", ignoreCase = true, useUnmergedTree = true).assertExists()
    }

    private fun androidx.compose.ui.test.junit4.AndroidComposeTestRule<*, *>.waitUntilExists(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = 5000
    ) {
        waitUntil(timeoutMillis) {
            onAllNodes(matcher, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
    }
}