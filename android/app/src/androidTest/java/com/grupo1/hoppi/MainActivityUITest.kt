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
        composeTestRule
            .onNodeWithText("Entrar", ignoreCase = true, useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun navigate_toMainApp_afterLogin() {
        composeTestRule
            .onNodeWithText("Entrar", ignoreCase = true, useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntilExists(hasContentDescription("Perfil", ignoreCase = true))

        composeTestRule
            .onNodeWithContentDescription("Perfil", ignoreCase = true)
            .assertExists()

    }

    private fun androidx.compose.ui.test.junit4.AndroidComposeTestRule<*, *>.waitUntilExists(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = 5000
    ) {
        waitUntil(timeoutMillis) {
            onAllNodes(matcher, useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }
}
